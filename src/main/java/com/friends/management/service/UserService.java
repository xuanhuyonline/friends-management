package com.friends.management.service;

import java.util.List;

public interface UserService {
    List<String> findFriendByEmail(String email);

    List<String> getCommonFriends(List<String> friends);

    List<String> findFriendSubscribedByEmail(String sender, String text);
}
