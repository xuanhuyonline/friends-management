package com.friends.management.controller;

import com.friends.management.security.jwt.AuthenticationEntryPointHandler;
import com.friends.management.security.jwt.JwtUtils;
import com.friends.management.security.service.UserDetailsServiceImpl;
import com.friends.management.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(UserManagementController.class)
public class UserManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthenticationEntryPointHandler authenticationEntryPointHandler;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    public void testFindFriendByEmail() throws Exception {
        String email = "test@gmail.com";
        List<String> friends = new ArrayList<>();
        friends.add("friend1@gmail.com");
        friends.add("friend2@gmail.com");

        when(userService.findFriendByEmail(email)).thenReturn(friends);

        mockMvc.perform(MockMvcRequestBuilders.get("/users").param("email", email))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.friends").value(friends))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(friends.size()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetCommonFriends() throws Exception {
        List<String> friends = new ArrayList<>();
        friends.add("friend1@gmail.com");
        friends.add("friend2@gmail.com");

        List<String> commonFriends = new ArrayList<>();
        commonFriends.add("common1friend@gmail.com");
        commonFriends.add("common2friend@gmail.com");

        when(userService.getCommonFriends(friends)).thenReturn(commonFriends);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/common")
                .param("friends", "friend1@gmail.com, friend2@gmail.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.friends").value(commonFriends))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(commonFriends.size()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFindFriendSubscribedByEmail() throws Exception {
        String sender = "test@gmail.com";
        String text = "Hello World! friend2@gmail.com, friend3@gmail.com";

        List<String> recipients = new ArrayList<>();
        recipients.add("friend1@gmail.com");
        recipients.add("friend2@gmail.com");
        recipients.add("friend3@gmail.com");

        when(userService.findFriendSubscribedByEmail(sender, text)).thenReturn(recipients);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/subscribed").param("sender", sender).param("text", text)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true)).andExpect(MockMvcResultMatchers.jsonPath("$.recipients").value(recipients));
    }
}
