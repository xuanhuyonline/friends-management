package com.friends.management.controller;

import com.friends.management.dto.SubscriptionRequest;
import com.friends.management.service.FriendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(FriendManagementController.class)
public class FriendManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendService friendService;

    @Test
    public void testCreateFriendsConnection() throws Exception {
        List<String> friends = new ArrayList<>();
        friends.add("friend1@gmail.com");
        friends.add("friend2@gmail.com");

        when(friendService.createFriendConnection(friends)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/friends").contentType(MediaType.APPLICATION_JSON).content("{\"friends\":[\"friend1@gmail.com\",\"friend2@gmail.com\"]}")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }

    @Test
    public void testCreateUpdateSubscription() throws Exception {
        when(friendService.createUpdateSubscription(any(SubscriptionRequest.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/friends/subscription").contentType(MediaType.APPLICATION_JSON).content("{\"requestor\":\"friend1@gmail.com\",\"target\":\"friend2@gmail.com\"}")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }

    @Test
    public void testBlockFriend() throws Exception {
        when(friendService.blockFriend(any(SubscriptionRequest.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/friends/block").contentType(MediaType.APPLICATION_JSON).content("{\"requestor\": \"friend1@gmail.com\", \"target\": \"friend2@gmail.com\" }")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }
}
