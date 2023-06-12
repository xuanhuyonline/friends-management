package com.friends.management.service;

import com.friends.management.entity.User;
import com.friends.management.exception.ApplicationException;
import com.friends.management.repository.FriendRepository;
import com.friends.management.repository.UserRepository;
import com.friends.management.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@RequiredArgsConstructor
public class FriendServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private FriendRepository friendRepository;

    @InjectMocks
    private FriendService friendService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnValidEmail() {
        String email = "lexuanhuy2k1@gmail.com";
        boolean result = Utils.isValidEmail(email);
        Assertions.assertTrue(result);
    }

    @Test
    public void shouldReturnInValidEmail() {
        String email = "invalid_email";
        boolean result = Utils.isValidEmail(email);
        Assertions.assertFalse(result);
    }

    @Test
    public void shouldReturnNonExistingEmail() {
        User user = null;
        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> {
            friendService.checkUsersEmail(user);
        });

        Assertions.assertEquals("Email address does not exist", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

    @Test
    public void testFindFriendByEmail_ValidEmail() {
        String email = "test@gmail.com";

        User user = new User();
        user.setId(1L);

        List<String> friendEmails = new ArrayList<>();
        friendEmails.add("friends1@gmail.com");
        friendEmails.add("friends2@gmail.com");

        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);
        Mockito.when(friendRepository.findFriendByEmail(user.getId())).thenReturn(friendEmails);

        List<String> result = friendService.findFriendByEmail(email);

        Assertions.assertEquals(2, result.size());
    }
    @Test
    public void testFindFriendByEmail_InvalidEmail(){
        String email = "invalid_email";
        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () ->{
            friendService.findFriendByEmail(email);
        });

        Assertions.assertEquals("Invalid email format", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getHttpStatus());
    }

}
