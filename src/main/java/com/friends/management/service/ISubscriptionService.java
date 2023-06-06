package com.friends.management.service;

import com.friends.management.common.ApiResponse;
import com.friends.management.dto.SubscriptionRequestDto;

public interface ISubscriptionService {
    ApiResponse createUpdateSubscription(SubscriptionRequestDto requestDto);
}
