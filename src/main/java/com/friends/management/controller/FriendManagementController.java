package com.friends.management.controller;

import com.friends.management.common.SuccessResponse;
import com.friends.management.dto.FriendRequest;
import com.friends.management.dto.SubscriptionRequest;
import com.friends.management.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("friends")
@RequiredArgsConstructor
public class FriendManagementController {
    private final FriendService friendService;

    @PostMapping
    public SuccessResponse createFriendsConnection(@RequestBody @Valid FriendRequest request) {
        Boolean success = friendService.createFriendConnection(request.getFriends());
        return new SuccessResponse(success);
    }

    @PostMapping("/subscription")
    public SuccessResponse createUpdateSubscription(@RequestBody @Valid SubscriptionRequest request) {
        Boolean success = friendService.createUpdateSubscription(request);
        return new SuccessResponse(success);
    }

    @PostMapping("/block")
    public SuccessResponse blockFriend(@RequestBody @Valid SubscriptionRequest request) {
        Boolean success = friendService.blockFriend(request);
        return new SuccessResponse(success);
    }

}