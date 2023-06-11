package com.friends.management;

import com.friends.management.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FriendsManagementApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void shouldReturnValidEmail() {
        String email = "lexuanhuy2k1@gmail.com";
        boolean result = Utils.isValidEmail(email);
        Assertions.assertTrue(result);
    }

    @Test
    void shouldReturnInValidEmail() {
        String email = "";
        boolean result = Utils.isValidEmail(email);
        Assertions.assertFalse(result);
    }

}
