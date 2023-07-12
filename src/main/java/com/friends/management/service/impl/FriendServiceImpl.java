package com.friends.management.service.impl;

import com.friends.management.dto.FriendStatus;
import com.friends.management.dto.SubscriptionRequest;
import com.friends.management.entity.Friend;
import com.friends.management.entity.User;
import com.friends.management.exception.ApplicationException;
import com.friends.management.repository.FriendRepository;
import com.friends.management.repository.UserRepository;
import com.friends.management.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.friends.management.utils.UtilsEmail.checkSameEmail;
import static com.friends.management.utils.UtilsEmail.isValidEmail;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    //Questions 1
    @Override
    @Transactional
    public Boolean createFriendConnection(List<String> friends) {
        if (friends.size() != 2) {
            throw new ApplicationException("Exactly two email addresses are required", HttpStatus.BAD_REQUEST.value());
        }

        if (!isValidEmail(friends.get(0)) || !isValidEmail(friends.get(1))) {
            throw new ApplicationException("Invalid email format", HttpStatus.BAD_REQUEST.value());
        }

        User user1 = userRepository.findByEmail(friends.get(0))
                .orElseThrow(() -> new ApplicationException("Email address does not exist with: " + friends.get(0), HttpStatus.BAD_REQUEST.value()));

        User user2 = userRepository.findByEmail(friends.get(1))
                .orElseThrow(() -> new ApplicationException("Email address does not exist with: " + friends.get(1), HttpStatus.BAD_REQUEST.value()));

        checkSameEmail(user1, user2);

        if (friendRepository.isBlockedEachOther(user1.getId(), user2.getId())) {
            throw new ApplicationException("Two users have blocked each other", HttpStatus.BAD_REQUEST.value());
        }

        if (friendRepository.isAlreadyFriends(user1.getId(), user2.getId())) {
            throw new ApplicationException("Friend connection already exists", HttpStatus.BAD_REQUEST.value());
        }

        createOrUpdateRelationShip(user1.getId(), user2.getId(), FriendStatus.FRIEND);
        createOrUpdateRelationShip(user2.getId(), user1.getId(), FriendStatus.FRIEND);
        return true;
    }

    private void createOrUpdateRelationShip(Long userId1, Long userId2, FriendStatus status) {
        Friend friend = friendRepository.friendStatus(userId1, userId2);

        if (friend == null) {
            addRelationship(userId1, userId2, status);
        } else {
            updateRelationship(userId1, userId2, status, friend);
        }
    }

    private void updateRelationship(Long userId1, Long userId2, FriendStatus status, Friend friend) {
        switch (status) {
            case FRIEND:
                //update: status is Subscriber
                populateFriend(friend, userId1, userId2, status, friend.getSubscriber());
                break;
            case RECEIVE_UPDATE:
                //update: status receive update
                populateFriend(friend, userId1, userId2, friend.getStatus(), true);
                break;
            case BLOCK:
                //update: status Block
                populateFriend(friend, userId1, userId2, status, false);
                break;
        }
        friendRepository.save(friend);
    }

    private void populateFriend(Friend friend, Long userId1, Long userId2, FriendStatus status, Boolean subscriber) {
        friend.setUser(new User(userId1));
        friend.setFriend(new User(userId2));
        friend.setStatus(status);
        friend.setSubscriber(subscriber);
    }

    private void addRelationship(Long userId1, Long userId2, FriendStatus status) {
        switch (status) {
            case FRIEND:
                //create new friend connection
                createFriend(userId1, userId2, status, false);
                break;
            case RECEIVE_UPDATE:
                //create new receive update
                createFriend(userId1, userId2, FriendStatus.NO_RELATIONSHIP, true);
                break;
            case BLOCK:
                //create new block
                createFriend(userId1, userId2, status, false);
                break;
        }
    }

    private void createFriend(Long userId1, Long userId2, FriendStatus friendStatus, boolean subscriber) {
        Friend friend = Friend.builder()
                .user(new User(userId1))
                .friend(new User(userId2))
                .status(friendStatus)
                .subscriber(subscriber)
                .build();
        friendRepository.save(friend);
    }

    //Questions 4
    @Transactional
    @Override
    public Boolean createUpdateSubscription(SubscriptionRequest request) {

        User requestor = userRepository.findByEmail(request.getRequestor())
                .orElseThrow(() -> new ApplicationException("Email address does not exist with: " + request.getRequestor(), HttpStatus.BAD_REQUEST.value()));

        User target = userRepository.findByEmail(request.getTarget())
                .orElseThrow(() -> new ApplicationException("Email address does not exist with: " + request.getTarget(), HttpStatus.BAD_REQUEST.value()));

        checkSameEmail(requestor, target);

        if (friendRepository.isBlockedEachOther(requestor.getId(), target.getId())) {
            throw new ApplicationException("Two users have blocked each other", HttpStatus.BAD_REQUEST.value());
        }

        if (friendRepository.isReceivedUpdate(requestor.getId(), target.getId())) {
            throw new ApplicationException("Subscription for updates already exists", HttpStatus.BAD_REQUEST.value());
        }

        createOrUpdateRelationShip(requestor.getId(), target.getId(), FriendStatus.RECEIVE_UPDATE);
        return true;
    }

    //Questions 5
    @Transactional
    @Override
    public Boolean blockFriend(SubscriptionRequest request) {
        User requestor = userRepository.findByEmail(request.getRequestor())
                .orElseThrow(() -> new ApplicationException("Email address does not exist with: " + request.getRequestor(), HttpStatus.BAD_REQUEST.value()));

        User target = userRepository.findByEmail(request.getTarget())
                .orElseThrow(() -> new ApplicationException("Email address does not exist with: " + request.getTarget(), HttpStatus.BAD_REQUEST.value()));

        checkSameEmail(requestor, target);

        if (friendRepository.isBlockedEachOther(requestor.getId(), target.getId())) {
            throw new ApplicationException("Two users have blocked each other", HttpStatus.BAD_REQUEST.value());
        }
        createOrUpdateRelationShip(requestor.getId(), target.getId(), FriendStatus.BLOCK);
        createOrUpdateRelationShip(target.getId(), requestor.getId(), FriendStatus.BLOCK);
        return true;
    }

}
