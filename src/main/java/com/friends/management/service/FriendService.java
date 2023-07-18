package com.friends.management.service;

import com.friends.management.dto.request.SubscriptionRequest;

import java.util.List;

public interface FriendService {
    Boolean createFriendConnection(List<String> friends);

    Boolean createUpdateSubscription(SubscriptionRequest request);

    Boolean blockFriend(SubscriptionRequest request);
}
