package com.friends.management.controller;

import com.friends.management.common.ApiResponse;
import com.friends.management.dto.FriendRequestDto;
import com.friends.management.dto.SubscriptionRequestDto;
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
    public ApiResponse createFriendsConnection(@RequestBody @Valid FriendRequestDto requestDto) {
            return friendService.createFriendConnection(requestDto.getFriends());
    }

    @GetMapping
    public ApiResponse findFriendByEmail(@RequestParam("email") String email) {
        return friendService.findFriendByEmail(email);
    }

    @PostMapping("/subscription")
    public ApiResponse createUpdateSubscription(@RequestBody @Valid SubscriptionRequestDto requestDto) {
        return friendService.createUpdateSubscription(requestDto);
    }

    @GetMapping("/common-friends")
    public ApiResponse getCommonFriends(@RequestBody @Valid FriendRequestDto requestDto) {
        return friendService.getCommonFriends(requestDto.getFriends());
    }

    @PostMapping("/block")
    public ApiResponse blockFriend(@RequestBody @Valid SubscriptionRequestDto requestDto) {
        return friendService.blockFriend(requestDto);
    }

}