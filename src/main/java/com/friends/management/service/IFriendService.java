package com.friends.management.service;

import com.friends.management.common.ApiResponse;
import com.friends.management.dto.SubscriptionRequestDto;

import java.util.List;

public interface IFriendService {
    ApiResponse createFriendConnection(List<String> friends);
    ApiResponse getFriendsList(String email);
    ApiResponse getCommonFriends(List<String> friends);
    ApiResponse blockUpdate(SubscriptionRequestDto requestDto);
}
