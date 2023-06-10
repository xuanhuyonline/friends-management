package com.friends.management.controller;

import com.friends.management.common.ApiResponse;
import com.friends.management.dto.FriendRequest;
import com.friends.management.dto.SenderRequest;
import com.friends.management.dto.SubscriptionRequest;
import com.friends.management.service.IFriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("friend-management")
@RequiredArgsConstructor
public class FriendManagementController {
    private final IFriendService friendService;

    @PostMapping
    public ApiResponse createFriendsConnection(@RequestBody @Valid FriendRequest request) {
            return friendService.createFriendConnection(request.getFriends());
    }

    @GetMapping
    public ApiResponse findFriendByEmail(@RequestParam("email") String email) {
        return friendService.findFriendByEmail(email);
    }

    @PostMapping("/subscription")
    public ApiResponse createUpdateSubscription(@RequestBody @Valid SubscriptionRequest request) {
        return friendService.createUpdateSubscription(request);
    }

    @GetMapping("/common-friends")
    public ApiResponse getCommonFriends(@RequestBody @Valid FriendRequest request) {
        return friendService.getCommonFriends(request.getFriends());
    }

    @PostMapping("/block")
    public ApiResponse blockFriend(@RequestBody @Valid SubscriptionRequest request) {
        return friendService.blockFriend(request);
    }

    @GetMapping("/friend-Subscribed")
    public ApiResponse findFriendSubscribedByEmail(@RequestBody @Valid SenderRequest request){
        return friendService.findFriendSubscribedByEmail(request);
    }

}