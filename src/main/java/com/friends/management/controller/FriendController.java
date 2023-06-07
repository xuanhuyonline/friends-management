package com.friends.management.controller;

import com.friends.management.common.ApiResponse;
import com.friends.management.dto.FriendRequestDto;
import com.friends.management.dto.SubscriptionRequestDto;
import com.friends.management.service.FriendService;
import com.friends.management.service.IFriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class FriendController {
    private final IFriendService friendService;

    @PostMapping("/create")
    public ApiResponse createFriendsConnection(@RequestBody @Valid FriendRequestDto requestDto) {
        try {
            return friendService.createFriendConnection(requestDto.getFriends());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage())).getBody();
        }
    }

    @GetMapping("/list")
    public ApiResponse getFriendsList(@RequestParam("email") String email){
        try {
            return friendService.getFriendsList(email);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage())).getBody();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An error occurred")).getBody();
        }
    }

    @PostMapping("/subscription")
    public ApiResponse createUpdateSubscription(@RequestBody @Valid SubscriptionRequestDto requestDto){
        try {
            return friendService.createUpdateSubscription(requestDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage())).getBody();
        }
    }

    @GetMapping("/common-friends")
    public ApiResponse getCommonFriends(@RequestBody @Valid FriendRequestDto requestDto){
        try {
            return friendService.getCommonFriends(requestDto.getFriends());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage())).getBody();
        }
    }

    @PostMapping("/block")
    public ApiResponse blockUpdate(@RequestBody @Valid SubscriptionRequestDto requestDto){
        try {
            return friendService.blockUpdate(requestDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage())).getBody();
        }
    }

}