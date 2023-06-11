package com.friends.management.controller;

import com.friends.management.common.ApiResponse;
import com.friends.management.dto.FriendRequest;
import com.friends.management.dto.SenderRequest;
import com.friends.management.dto.SubscriptionRequest;
import com.friends.management.service.IFriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("friend-management")
@RequiredArgsConstructor
public class FriendManagementController {
    private final IFriendService friendService;

    @PostMapping
    public ApiResponse createFriendsConnection(@RequestBody @Valid FriendRequest request) {
        Boolean success = friendService.createFriendConnection(request.getFriends());
        return new ApiResponse(success);
    }

    @GetMapping
    public ApiResponse findFriendByEmail(@RequestParam("email") String email) {
        List<String> friends = friendService.findFriendByEmail(email);
        return new ApiResponse(true, friends, friends.size());
    }

    @GetMapping("/common-friends")
    public ApiResponse getCommonFriends(@RequestBody @Valid FriendRequest request) {
        List<String> friends = friendService.getCommonFriends(request.getFriends());
        return new ApiResponse(true, friends, friends.size());
    }

    @PostMapping("/subscription")
    public ApiResponse createUpdateSubscription(@RequestBody @Valid SubscriptionRequest request) {
        Boolean success = friendService.createUpdateSubscription(request);
        return new ApiResponse(success);
    }

    @PostMapping("/block")
    public ApiResponse blockFriend(@RequestBody @Valid SubscriptionRequest request) {
        Boolean success = friendService.blockFriend(request);
        return new ApiResponse(success);
    }

    @GetMapping("/friend-Subscribed")
    public ApiResponse findFriendSubscribedByEmail(@RequestBody @Valid SenderRequest request) {
        List<String> recipients = friendService.findFriendSubscribedByEmail(request);
        return new ApiResponse(true, recipients);
    }

}