package com.friends.management.service;

import com.friends.management.common.ApiResponse;

import java.util.List;

public interface IFriendService {
    void createFriendConnection(List<String> friends);
    ApiResponse getFriendsList(String email);

    ApiResponse getCommonFriends(List<String> friends);
}
