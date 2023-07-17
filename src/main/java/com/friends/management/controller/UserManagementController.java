package com.friends.management.controller;

import com.friends.management.response.EmailRecipientsResponse;
import com.friends.management.response.FriendsListResponse;
import com.friends.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserManagementController {
    private final UserService userService;
    @GetMapping
    public FriendsListResponse findFriendByEmail(@RequestParam("email") String email) {
        List<String> friends = userService.findFriendByEmail(email);
        return new FriendsListResponse(true,friends, friends.size());
    }

    @GetMapping("/common")
    public FriendsListResponse getCommonFriends(@RequestParam("friends") List<String> friends) {
        List<String> commonFriends = userService.getCommonFriends(friends);
        return new FriendsListResponse(true, commonFriends, commonFriends.size());
    }

    @GetMapping("/subscribed")
    public EmailRecipientsResponse findFriendSubscribedByEmail(@RequestParam("sender") String sender, @RequestParam("text") String text) {
        List<String> recipients = userService.findFriendSubscribedByEmail(sender, text);
        return new EmailRecipientsResponse(true, recipients);
    }
}
