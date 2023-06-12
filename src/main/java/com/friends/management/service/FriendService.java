package com.friends.management.service;

import com.friends.management.dto.FriendStatus;
import com.friends.management.dto.SubscriptionRequest;
import com.friends.management.entity.Friend;
import com.friends.management.entity.User;
import com.friends.management.exception.ApplicationException;
import com.friends.management.repository.FriendRepository;
import com.friends.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.friends.management.utils.Utils.isValidEmail;

@Service
@RequiredArgsConstructor
public class FriendService implements IFriendService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    //Questions 1
    @Override
    @Transactional
    public Boolean createFriendConnection(List<String> friends) {
        if (!isValidEmail(friends.get(0)) || !isValidEmail(friends.get(1))) {
            throw new ApplicationException("Invalid email format", HttpStatus.BAD_REQUEST.value());
        }

        if (friends.size() != 2) {
            throw new ApplicationException("Exactly two email addresses are required", HttpStatus.BAD_REQUEST.value());
        }

        User user1 = userRepository.findByEmail(friends.get(0));
        User user2 = userRepository.findByEmail(friends.get(1));

        checkUsersEmail(user1, user2);

        if (friendRepository.areFriends(user1.getId(), user2.getId())) {
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
            //check block
            if (friend.getStatus().equals(FriendStatus.BLOCK)) {
                throw new ApplicationException("Two users have blocked each other", HttpStatus.BAD_REQUEST.value());
            }

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
        Friend friend = new Friend(new User(userId1), new User(userId2), friendStatus, subscriber);
        friendRepository.save(friend);
    }

    private void checkUsersEmail(User user1, User user2) {
        if (user1 == null || user2 == null) {
            throw new ApplicationException("One or both email addresses do not exist", HttpStatus.BAD_REQUEST.value());
        }

        if (user1 == user2) {
            throw new ApplicationException("Two emails cannot be the same", HttpStatus.BAD_REQUEST.value());
        }
    }

    //Questions 2
    @Override
    public List<String> findFriendByEmail(String email) {
        if (!isValidEmail(email)) {
            throw new ApplicationException("Invalid email format", HttpStatus.BAD_REQUEST.value());
        }

        User user = userRepository.findByEmail(email);

        checkUsersEmail(user);

        return friendRepository.findFriendByEmail(user.getId());
    }

    //Questions 3
    @Override
    public List<String> getCommonFriends(List<String> friends) {
        if (!isValidEmail(friends.get(0)) || !isValidEmail(friends.get(1))) {
            throw new ApplicationException("Invalid email format", HttpStatus.BAD_REQUEST.value());
        }

        if (friends.size() != 2) {
            throw new ApplicationException("Exactly two email addresses are required", HttpStatus.BAD_REQUEST.value());
        }

        User user1 = userRepository.findByEmail(friends.get(0));
        User user2 = userRepository.findByEmail(friends.get(1));

        checkUsersEmail(user1, user2);

        return friendRepository.findCommonEmails(user1.getId(), user2.getId());
    }

    //Questions 4
    @Transactional
    @Override
    public Boolean createUpdateSubscription(SubscriptionRequest request) {

        User requestor = userRepository.findByEmail(request.getRequestor());
        User target = userRepository.findByEmail(request.getTarget());

        checkUsersEmail(requestor, target);

        if (friendRepository.receivedUpdate(requestor.getId(), target.getId())) {
            throw new ApplicationException("Subscription for updates already exists", HttpStatus.BAD_REQUEST.value());
        }

        createOrUpdateRelationShip(requestor.getId(), target.getId(), FriendStatus.RECEIVE_UPDATE);
        return true;
    }

    //Questions 5
    @Transactional
    @Override
    public Boolean blockFriend(SubscriptionRequest request) {
        User requestor = userRepository.findByEmail(request.getRequestor());
        User target = userRepository.findByEmail(request.getTarget());

        checkUsersEmail(requestor, target);

        if (friendRepository.blockedEachOther(requestor.getId(), target.getId())) {
            throw new ApplicationException("Two users have blocked each other", HttpStatus.BAD_REQUEST.value());
        }
        createOrUpdateRelationShip(requestor.getId(), target.getId(), FriendStatus.BLOCK);
        createOrUpdateRelationShip(target.getId(), requestor.getId(), FriendStatus.BLOCK);
        return true;
    }

    //Questions 6
    @Override
    public List<String> findFriendSubscribedByEmail(String sender, String text) {
        if (!isValidEmail(sender)) {
            throw new ApplicationException("Invalid email format", HttpStatus.BAD_REQUEST.value());
        }

        User user = userRepository.findByEmail(sender);

        checkUsersEmail(user);

        List<String> recipients = friendRepository.findFriendSubscribedByEmail(user.getId());

        emailExtractor(text, recipients);

        return recipients;
    }

    private void emailExtractor(String text, List<String> recipients) {
        String regex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String email = matcher.group();
            recipients.add(email);
        }
    }

    private void checkUsersEmail(User user) {
        if (user == null) {
            throw new ApplicationException("Email address does not exist", HttpStatus.BAD_REQUEST.value());
        }
    }

}
