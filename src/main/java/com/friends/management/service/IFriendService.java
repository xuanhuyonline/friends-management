package com.friends.management.service;

import com.friends.management.common.ApiResponse;
import com.friends.management.dto.SenderRequestDto;
import com.friends.management.dto.SubscriptionRequestDto;

import java.util.List;

public interface IFriendService {
    ApiResponse createFriendConnection(List<String> friends);

    ApiResponse findFriendByEmail(String email);

    ApiResponse getCommonFriends(List<String> friends);

    ApiResponse createUpdateSubscription(SubscriptionRequestDto requestDto);

    ApiResponse blockFriend(SubscriptionRequestDto requestDto);

    ApiResponse findFriendSubscribedByEmail(SenderRequestDto requestDto);
}
