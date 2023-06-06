package com.friends.management.controller;

import com.friends.management.common.ApiResponse;
import com.friends.management.dto.SubscriptionRequestDto;
import com.friends.management.service.ISubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class SubscriptionController {
    private final ISubscriptionService subscriptionService;

    @PostMapping("/subscription")
    public ApiResponse createUpdateSubscription(@RequestBody @Valid SubscriptionRequestDto requestDto){
        try {
            return subscriptionService.createUpdateSubscription(requestDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage())).getBody();
        }
    }
}
