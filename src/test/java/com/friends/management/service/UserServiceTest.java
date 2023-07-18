package com.friends.management.service;

import com.friends.management.entity.User;
import com.friends.management.exception.ApplicationException;
import com.friends.management.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    //Questions 2
    @Test
    public void testFindFriendByEmail_ValidEmail() {
        String email = "test@gmail.com";

        Optional<User> user = Optional.of(new User());
        user.get().setId(1L);

        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("friend1@gmail.com");
        friendEmails.add("friend2@gmail.com");

        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);
        Mockito.when(userRepository.findFriendByEmail(user.get().getId())).thenReturn(friendEmails);

        List<String> result = userService.findFriendByEmail(email);

        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void testFindFriendByEmail_InvalidEmail() {
        String email = "invalid_email";

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            userService.findFriendByEmail(email);
        });

        Assertions.assertEquals("Invalid email format", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testFindFriendByEmail_EmptyEmail() {
        String email = "";

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            userService.findFriendByEmail(email);
        });

        Assertions.assertEquals("Email cannot be empty", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testFindFriendByEmail_NonExistingEmail() {
        String email = "friend@gmail.com";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            userService.findFriendByEmail(email);
        });

        Assertions.assertEquals("Email address does not exist with: " + email, exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testFindFriendByEmail_NoFriends() {
        String email = "friend@gmail.com";

        Optional<User> user = Optional.of(new User());
        List<String> friendEmails = new ArrayList<>();

        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);
        Mockito.when(userRepository.findFriendByEmail(user.get().getId())).thenReturn(friendEmails);

        List<String> result = userService.findFriendByEmail(email);

        Assertions.assertEquals(0, result.size());
    }

    //Questions 3
    @Test
    public void testGetCommonFriends_ValidEmail() {
        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("friend1@gmail.com");
        friendEmails.add("friend2@gmail.com");

        Optional<User> user1 = Optional.of(new User());
        Optional<User> user2 = Optional.of(new User());

        List<String> commonFriends = new ArrayList<>();
        commonFriends.add("commonfriend@gmail.com");

        Mockito.when(userRepository.findByEmail(friendEmails.get(0))).thenReturn(user1);
        Mockito.when(userRepository.findByEmail(friendEmails.get(1))).thenReturn(user2);

        Mockito.when(userRepository.findCommonEmails(user1.get().getId(), user2.get().getId())).thenReturn(commonFriends);

        List<String> result = userService.getCommonFriends(friendEmails);

        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void testGetCommonFriends_InvalidEmail() {
        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("invalid_email1");
        friendEmails.add("invalid_email2");

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            userService.getCommonFriends(friendEmails);
        });

        Assertions.assertEquals("Invalid email format", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testGetCommonFriends_EmptyEmail() {
        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("");
        friendEmails.add("");

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            userService.getCommonFriends(friendEmails);
        });

        Assertions.assertEquals("Email cannot be empty", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testGetCommonFriends_NotExactlyTwoEmails() {
        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("friend1@gmail.com");
        friendEmails.add("friends@gmail.com");
        friendEmails.add("friend3@gmail.com");

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            userService.getCommonFriends(friendEmails);
        });

        Assertions.assertEquals("Exactly two email addresses are required", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testGetCommonFriends_NonExistingEmail() {
        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("friend1@gmail.com");
        friendEmails.add("friend2@gmail.com");

        Mockito.when(userRepository.findByEmail(friendEmails.get(0))).thenReturn(Optional.empty());

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            userService.getCommonFriends(friendEmails);
        });

        Assertions.assertEquals("Email address does not exist with: " + friendEmails.get(0), exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testGetCommonFriends_NoFriends() {
        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("friend1@gmail.com");
        friendEmails.add("friend2@gmail.com");

        Optional<User> user1 = Optional.of(new User());
        Optional<User> user2 = Optional.of(new User());

        List<String> commonFriends = new ArrayList<>();

        Mockito.when(userRepository.findByEmail(friendEmails.get(0))).thenReturn(user1);
        Mockito.when(userRepository.findByEmail(friendEmails.get(1))).thenReturn(user2);
        Mockito.when(userRepository.findCommonEmails(user1.get().getId(), user2.get().getId())).thenReturn(commonFriends);

        List<String> result = userService.getCommonFriends(friendEmails);

        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void testGetCommonFriends_SameEmail() {
        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("friend@gmail.com");
        friendEmails.add("friend@gmail.com");

        Optional<User> user1 = Optional.of(new User());
        Optional<User> user2 = Optional.of(new User());

        Mockito.when(userRepository.findByEmail(friendEmails.get(0))).thenReturn(user1);
        Mockito.when(userRepository.findByEmail(friendEmails.get(1))).thenReturn(user2);

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            userService.getCommonFriends(friendEmails);
        });

        Assertions.assertEquals("Two emails cannot be the same", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    //Questions 6
    @Test
    public void testFindFriendSubscribedByEmail_ValidEmail() {
        String senderEmail = "sender@gmail.com";
        String text = "Hello World! kate@example.com";

        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("friend@gmail.com");

        Optional<User> user = Optional.of(new User());

        Mockito.when(userRepository.findByEmail(senderEmail)).thenReturn(user);
        Mockito.when(userRepository.findFriendSubscribedByEmail(user.get().getId())).thenReturn(friendEmails);

        List<String> result = userService.findFriendSubscribedByEmail(senderEmail, text);

        List<String> expectedRecipients = new ArrayList<>();
        expectedRecipients.add("friend@gmail.com");
        expectedRecipients.add("kate@example.com");

        Assertions.assertEquals(expectedRecipients, result);
    }

    @Test
    public void testFindFriendSubscribedByEmail_EmptyEmail() {
        String senderEmail = "";
        String text = "Hello World! kate@example.com";

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            userService.findFriendSubscribedByEmail(senderEmail, text);
        });

        Assertions.assertEquals("Email cannot be empty", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testFindFriendSubscribedByEmail_InValidEmail() {
        String senderEmail = "invalid_email";
        String text = "Hello World! kate@example.com";

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            userService.findFriendSubscribedByEmail(senderEmail, text);
        });
        Assertions.assertEquals("Invalid email format", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testFindFriendSubscribedByEmail_NonExistingEmail() {
        String senderEmail = "friend@gmail.com";
        String text = "Hello World! kate@example.com";

        Mockito.when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            userService.findFriendSubscribedByEmail(senderEmail, text);
        });

        Assertions.assertEquals("Email address does not exist with: " + senderEmail, exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testFindFriendSubscribedByEmail_NoFriends() {
        String senderEmail = "friend@gmail.com";
        String text = "Hello World!";

        List<String> friendEmails = new ArrayList<>();

        Optional<User> user = Optional.of(new User());

        Mockito.when(userRepository.findByEmail(senderEmail)).thenReturn(user);
        Mockito.when(userRepository.findFriendSubscribedByEmail(user.get().getId())).thenReturn(friendEmails);

        List<String> result = userService.findFriendSubscribedByEmail(senderEmail, text);

        List<String> expectedRecipients = new ArrayList<>();

        Assertions.assertEquals(expectedRecipients, result);
    }


}
