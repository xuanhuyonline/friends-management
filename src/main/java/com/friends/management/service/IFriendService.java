package com.friends.management.service;

import com.friends.management.common.ApiResponse;
import com.friends.management.dto.SenderRequest;
import com.friends.management.dto.SubscriptionRequest;

import java.util.List;

public interface IFriendService {
    Boolean createFriendConnection(List<String> friends);

    List<String> findFriendByEmail(String email);

    List<String> getCommonFriends(List<String> friends);

    Boolean createUpdateSubscription(SubscriptionRequest request);

    Boolean blockFriend(SubscriptionRequest request);

    List<String> findFriendSubscribedByEmail(SenderRequest request);
}
