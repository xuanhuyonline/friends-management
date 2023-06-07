package com.friends.management.service;

import com.friends.management.common.ApiResponse;
import com.friends.management.dto.FriendStatus;
import com.friends.management.dto.SubscriptionRequestDto;
import com.friends.management.entity.Friend;
import com.friends.management.entity.User;
import com.friends.management.repository.FriendRepository;
import com.friends.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService implements IFriendService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Override
    @Transactional
    public ApiResponse createFriendConnection(List<String> friends) {
        if (friends.size() != 2) {
            throw new IllegalArgumentException("Exactly two email addresses are required");
        }

        User user1 = userRepository.findByEmail(friends.get(0));
        User user2 = userRepository.findByEmail(friends.get(1));

        checkUsersEmail(user1, user2);

        if (friendRepository.areFriends(user1.getId(), user2.getId())) {
            throw new IllegalArgumentException("Friend connection already exists");
        }

        createOrUpdateRelationShip(user1.getId(), user2.getId(), FriendStatus.FRIEND.toString());
        createOrUpdateRelationShip(user2.getId(), user1.getId(), FriendStatus.FRIEND.toString());
        return new ApiResponse(true);
    }

    private void createOrUpdateRelationShip(Long userId1, Long userId2, String post) {
        Friend friendStatus = friendRepository.friendStatus(userId1, userId2);

        if (friendStatus == null) {
            addRelationship(userId1, userId2, post);
        } else {
            //check block
            if (friendStatus.getStatus().equals(FriendStatus.BLOCK)) {
                throw new IllegalArgumentException("Two users have blocked each other");
            }

            Friend friend = friendRepository.findById(friendStatus.getId()).orElseThrow();

            updateRelationship(userId1, userId2, post, friendStatus, friend);
        }
    }

    private void updateRelationship(Long userId1, Long userId2, String post, Friend friendStatus, Friend friend) {
        if (post.equals(FriendStatus.FRIEND.toString())) {
            //update: status is Subscriber

            populateFriend(friend, userId1, userId2, FriendStatus.FRIEND, friendStatus.getSubscriber());
        } else if (post.equals(FriendStatus.RECEIVE_UPDATE.toString())) {
            //update: status receive update

            populateFriend(friend, userId1, userId2, friendStatus.getStatus(), true);
        } else if (post.equals(FriendStatus.BLOCK.toString())) {
            //update: status Block

            populateFriend(friend, userId1, userId2, FriendStatus.BLOCK, false);
        }
        friendRepository.save(friend);
    }

    private void populateFriend(Friend friend, Long userId1, Long userId2, FriendStatus friend1, Boolean friendStatus) {
        friend.setUser(new User(userId1));
        friend.setFriend(new User(userId2));
        friend.setStatus(friend1);
        friend.setSubscriber(friendStatus);
    }

    private void addRelationship(Long userId1, Long userId2, String post) {
        if (post.equals(FriendStatus.FRIEND.toString())) {
            //create new friend connection
            createFriend(userId1, userId2, FriendStatus.FRIEND, false);
        } else if (post.equals(FriendStatus.RECEIVE_UPDATE.toString())) {
            //create new receive update
            createFriend(userId1, userId2, FriendStatus.NO_RELATIONSHIP, true);
        } else if (post.equals(FriendStatus.BLOCK.toString())) {
            //create new block
            createFriend(userId1, userId2, FriendStatus.BLOCK, false);
        }
    }

    private void createFriend(Long userId1, Long userId2, FriendStatus friendStatus, boolean subscriber) {
        Friend friend = new Friend(new User(userId1), new User(userId2), friendStatus, subscriber);
        friendRepository.save(friend);
    }

    private void checkUsersEmail(User user1, User user2) {
        if (user1 == null || user2 == null) {
            throw new IllegalArgumentException("One or both email addresses do not exist");
        }

        if (user1 == user2) {
            throw new IllegalArgumentException("Two emails cannot be the same");
        }
    }

    @Override
    public ApiResponse findFriendByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("Email address does not exist");
        }

        List<String> friends = friendRepository.findFriendByEmail(user.getId());
        int count = friends.size();

        return new ApiResponse(true, friends, count);
    }

    @Override
    public ApiResponse getCommonFriends(List<String> friends) {
        if (friends.size() != 2) {
            throw new IllegalArgumentException("Exactly two email addresses are required");
        }

        User user1 = userRepository.findByEmail(friends.get(0));
        User user2 = userRepository.findByEmail(friends.get(1));

        checkUsersEmail(user1, user2);

        List<String> commonFriends = friendRepository.findCommonEmails(user1.getId(), user2.getId());
        int count = commonFriends.size();
        return new ApiResponse(true, commonFriends, count);
    }

    @Transactional
    @Override
    public ApiResponse createUpdateSubscription(SubscriptionRequestDto requestDto) {

        User requestor = userRepository.findByEmail(requestDto.getRequestor());
        User target = userRepository.findByEmail(requestDto.getTarget());

        checkUsersEmail(requestor, target);

        if (friendRepository.receivedUpdate(requestor.getId(), target.getId())) {
            throw new IllegalArgumentException("Subscription for updates already exists");
        }

        createOrUpdateRelationShip(requestor.getId(), target.getId(), FriendStatus.RECEIVE_UPDATE.toString());
        return new ApiResponse(true);
    }

    @Transactional
    @Override
    public ApiResponse blockFriend(SubscriptionRequestDto requestDto) {
        User requestor = userRepository.findByEmail(requestDto.getRequestor());
        User target = userRepository.findByEmail(requestDto.getTarget());

        checkUsersEmail(requestor, target);

        if (friendRepository.blockedEachOther(requestor.getId(), target.getId())) {
            throw new IllegalArgumentException("Two users have blocked each other");
        }
        createOrUpdateRelationShip(requestor.getId(), target.getId(), FriendStatus.BLOCK.toString());
        createOrUpdateRelationShip(target.getId(), requestor.getId(), FriendStatus.BLOCK.toString());
        return new ApiResponse(true);
    }

}
