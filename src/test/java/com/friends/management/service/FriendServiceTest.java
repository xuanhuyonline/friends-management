package com.friends.management.service;

import com.friends.management.dto.SubscriptionRequest;
import com.friends.management.entity.Friend;
import com.friends.management.entity.User;
import com.friends.management.exception.ApplicationException;
import com.friends.management.repository.FriendRepository;
import com.friends.management.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class FriendServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendRepository friendRepository;

    @InjectMocks
    private FriendServiceImpl friendService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    //Questions 1
    @Test
    public void testCreateFriendConnection_ValidEmail() {
        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("friend1@gmail.com");
        friendEmails.add("friend2@gmail.com");

        Optional<User> user1 = Optional.of(new User());
        Optional<User> user2 = Optional.of(new User());

        Mockito.when(userRepository.findByEmail(friendEmails.get(0))).thenReturn(user1);
        Mockito.when(userRepository.findByEmail(friendEmails.get(1))).thenReturn(user2);

        Mockito.when(friendRepository.isBlockedEachOther(user1.get().getId(), user2.get().getId())).thenReturn(false);
        Mockito.when(friendRepository.isAlreadyFriends(user1.get().getId(), user2.get().getId())).thenReturn(false);

        Boolean result = friendService.createFriendConnection(friendEmails);

        Assertions.assertTrue(result);
    }

    @Test
    public void testUpdateFriendConnection_ValidEmail() {
        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("friend1@gmail.com");
        friendEmails.add("friend2@gmail.com");

        Optional<User> user1 = Optional.of(new User());
        Optional<User> user2 = Optional.of(new User());

        Mockito.when(userRepository.findByEmail(friendEmails.get(0))).thenReturn(user1);
        Mockito.when(userRepository.findByEmail(friendEmails.get(1))).thenReturn(user2);

        Mockito.when(friendRepository.isBlockedEachOther(user1.get().getId(), user2.get().getId())).thenReturn(false);
        Mockito.when(friendRepository.isAlreadyFriends(user1.get().getId(), user2.get().getId())).thenReturn(false);

        Friend friend = new Friend();
        Mockito.when(friendRepository.friendStatus(user1.get().getId(), user2.get().getId())).thenReturn(friend);

        Boolean result = friendService.createFriendConnection(friendEmails);

        Assertions.assertTrue(result);
    }

    @Test
    public void testCreateFriendConnection_InValidEmail() {
        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("invalid_email1");
        friendEmails.add("invalid_email2");

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            friendService.createFriendConnection(friendEmails);
        });

        Assertions.assertEquals("Invalid email format", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testCreateFriendConnection_NotExactlyTwoEmails() {
        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("friend1@gmail.com");
        friendEmails.add("friend2@gmail.com");
        friendEmails.add("friend3@gmail.com");

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            friendService.createFriendConnection(friendEmails);
        });

        Assertions.assertEquals("Exactly two email addresses are required", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testCreateFriendConnection_NonExistingEmail() {
        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("friend1@gmail.com");
        friendEmails.add("friend2@gmail.com");

        Mockito.when(userRepository.findByEmail(friendEmails.get(0))).thenReturn(Optional.empty());

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            friendService.createFriendConnection(friendEmails);
        });

        Assertions.assertEquals("Email address does not exist with: " + friendEmails.get(0), exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testCreateFriendConnection_SameEmail() {
        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("friend@gmail.com");
        friendEmails.add("friend@gmail.com");

        Optional<User> user1 = Optional.of(new User());
        Optional<User> user2 = Optional.of(new User());

        Mockito.when(userRepository.findByEmail(friendEmails.get(0))).thenReturn(user1);
        Mockito.when(userRepository.findByEmail(friendEmails.get(1))).thenReturn(user2);

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            friendService.createFriendConnection(friendEmails);
        });

        Assertions.assertEquals("Two emails cannot be the same", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testCreateFriendConnection_BlockedEachOther() {
        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("friend1@gmail.com");
        friendEmails.add("friend2@gmail.com");

        Optional<User> user1 = Optional.of(new User());
        Optional<User> user2 = Optional.of(new User());

        Mockito.when(userRepository.findByEmail(friendEmails.get(0))).thenReturn(user1);
        Mockito.when(userRepository.findByEmail(friendEmails.get(1))).thenReturn(user2);
        Mockito.when(friendRepository.isBlockedEachOther(user1.get().getId(), user2.get().getId())).thenReturn(true);

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            friendService.createFriendConnection(friendEmails);
        });

        Assertions.assertEquals("Two users have blocked each other", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testCreateFriendConnection_AlreadyFriends() {
        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("friend1@gmail.com");
        friendEmails.add("friend2@gmail.com");

        Optional<User> user1 = Optional.of(new User());
        Optional<User> user2 = Optional.of(new User());

        Mockito.when(userRepository.findByEmail(friendEmails.get(0))).thenReturn(user1);
        Mockito.when(userRepository.findByEmail(friendEmails.get(1))).thenReturn(user2);

        Mockito.when(friendRepository.isBlockedEachOther(user1.get().getId(), user2.get().getId())).thenReturn(false);
        Mockito.when(friendRepository.isAlreadyFriends(user1.get().getId(), user2.get().getId())).thenReturn(true);

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            friendService.createFriendConnection(friendEmails);
        });

        Assertions.assertEquals("Friend connection already exists", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    //Questions 4
    @Test
    public void testCreateUpdateSubscription_ValidEmail() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setRequestor("friend1@gmail.com");
        request.setTarget("friend2@gmail.com");

        Optional<User> requestor = Optional.of(new User());
        Optional<User> target = Optional.of(new User());

        Mockito.when(userRepository.findByEmail(request.getRequestor())).thenReturn(requestor);
        Mockito.when(userRepository.findByEmail(request.getTarget())).thenReturn(target);

        Mockito.when(friendRepository.isBlockedEachOther(requestor.get().getId(), target.get().getId())).thenReturn(false);
        Mockito.when(friendRepository.isReceivedUpdate(requestor.get().getId(), target.get().getId())).thenReturn(false);

        Boolean result = friendService.createUpdateSubscription(request);

        Assertions.assertTrue(result);
    }

    @Test
    public void testCreateUpdateSubscription_NonExistingEmail() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setRequestor("friend1@gmail.com");

        Mockito.when(userRepository.findByEmail(request.getRequestor())).thenReturn(Optional.empty());

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            friendService.createUpdateSubscription(request);
        });

        Assertions.assertEquals("Email address does not exist with: " + request.getRequestor(), exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testCreateUpdateSubscription_SameEmail() {
        //
        SubscriptionRequest request = new SubscriptionRequest();
        request.setRequestor("friend@gmail.com");
        request.setTarget("friend@gmail.com");

        Optional<User> requestor = Optional.of(new User());
        Optional<User> target = Optional.of(new User());

        Mockito.when(userRepository.findByEmail(request.getRequestor())).thenReturn(requestor);
        Mockito.when(userRepository.findByEmail(request.getTarget())).thenReturn(target);

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            friendService.createUpdateSubscription(request);
        });

        Assertions.assertEquals("Two emails cannot be the same", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testCreateUpdateSubscription_BlockedEachOther() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setRequestor("friend1@gmail.com");
        request.setTarget("friend2@gmail.com");

        Optional<User> requestor = Optional.of(new User());
        Optional<User> target = Optional.of(new User());

        Mockito.when(userRepository.findByEmail(request.getRequestor())).thenReturn(requestor);
        Mockito.when(userRepository.findByEmail(request.getTarget())).thenReturn(target);

        Mockito.when(friendRepository.isBlockedEachOther(requestor.get().getId(), target.get().getId())).thenReturn(true);

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            friendService.createUpdateSubscription(request);
        });

        Assertions.assertEquals("Two users have blocked each other", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testCreateUpdateSubscription_ReceivedUpdate() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setRequestor("friend1@gmail.com");
        request.setTarget("friend2@gmail.com");

        Optional<User> requestor = Optional.of(new User());
        Optional<User> target = Optional.of(new User());

        Mockito.when(userRepository.findByEmail(request.getRequestor())).thenReturn(requestor);
        Mockito.when(userRepository.findByEmail(request.getTarget())).thenReturn(target);

        Mockito.when(friendRepository.isBlockedEachOther(requestor.get().getId(), target.get().getId())).thenReturn(false);
        Mockito.when(friendRepository.isReceivedUpdate(requestor.get().getId(), target.get().getId())).thenReturn(true);

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            friendService.createUpdateSubscription(request);
        });

        Assertions.assertEquals("Subscription for updates already exists", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    //Questions 5
    @Test
    public void testBlockFriend_ValidEmail() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setRequestor("friend1@gmail.com");
        request.setTarget("friend2@gmail.com");

        Optional<User> requestor = Optional.of(new User());
        Optional<User> target = Optional.of(new User());

        Mockito.when(userRepository.findByEmail(request.getRequestor())).thenReturn(requestor);
        Mockito.when(userRepository.findByEmail(request.getTarget())).thenReturn(target);

        Mockito.when(friendRepository.isBlockedEachOther(requestor.get().getId(), target.get().getId())).thenReturn(false);

        Boolean result = friendService.blockFriend(request);

        Assertions.assertTrue(result);
    }

    @Test
    public void testBlockFriend_NonExistingEmail() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setRequestor("friend1@gmail.com");

        Mockito.when(userRepository.findByEmail(request.getRequestor())).thenReturn(Optional.empty());

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            friendService.createUpdateSubscription(request);
        });

        Assertions.assertEquals("Email address does not exist with: " + request.getRequestor(), exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testBlockFriend_SameEmail() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setRequestor("friend@gmail.com");
        request.setTarget("friend@gmail.com");

        Optional<User> requestor = Optional.of(new User());
        Optional<User> target = Optional.of(new User());

        Mockito.when(userRepository.findByEmail(request.getRequestor())).thenReturn(requestor);
        Mockito.when(userRepository.findByEmail(request.getTarget())).thenReturn(target);

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            friendService.createUpdateSubscription(request);
        });

        Assertions.assertEquals("Two emails cannot be the same", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testBlockFriend_BlockedEachOther() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setRequestor("friend1@gmail.com");
        request.setTarget("friend2@gmail.com");

        Optional<User> requestor = Optional.of(new User());
        Optional<User> target = Optional.of(new User());

        Mockito.when(userRepository.findByEmail(request.getRequestor())).thenReturn(requestor);
        Mockito.when(userRepository.findByEmail(request.getTarget())).thenReturn(target);

        Mockito.when(friendRepository.isBlockedEachOther(requestor.get().getId(), target.get().getId())).thenReturn(true);

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            friendService.createUpdateSubscription(request);
        });

        Assertions.assertEquals("Two users have blocked each other", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }
}
