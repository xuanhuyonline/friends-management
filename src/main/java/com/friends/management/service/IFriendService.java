package com.friends.management.service;

import com.friends.management.common.ApiResponse;
import com.friends.management.dto.SenderRequest;
import com.friends.management.dto.SubscriptionRequest;

import java.util.List;

public interface IFriendService {
    ApiResponse createFriendConnection(List<String> friends);

    ApiResponse findFriendByEmail(String email);

    ApiResponse getCommonFriends(List<String> friends);

    ApiResponse createUpdateSubscription(SubscriptionRequest requestDto);

    ApiResponse blockFriend(SubscriptionRequest requestDto);

    ApiResponse findFriendSubscribedByEmail(SenderRequest requestDto);
}
