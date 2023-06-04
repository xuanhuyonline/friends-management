package com.friends.management.controller;

import com.friends.management.common.ApiResponse;
import com.friends.management.dto.FriendRequestDto;
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
    public ResponseEntity<?> createFriensConnection(@RequestBody @Valid FriendRequestDto requestDto) {
        try {
            friendService.createFriendConnection(requestDto.getFriends());
            return ResponseEntity.ok().body(new ApiResponse(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

}
