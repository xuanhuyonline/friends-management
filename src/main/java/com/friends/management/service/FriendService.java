package com.friends.management.service;

import com.friends.management.dto.FriendStatus;
import com.friends.management.entity.Friend;
import com.friends.management.entity.User;
import com.friends.management.repository.FriendRepository;
import com.friends.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService implements IFriendService{
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Override
    @Transactional
    public void createFriendConnection(List<String> friends) {
        if (friends.size() != 2) {
            throw new IllegalArgumentException("Exactly two email addresses are required");
        }

        User user1 = userRepository.findByEmail(friends.get(0));
        User user2 = userRepository.findByEmail(friends.get(1));

        if(user1 == user2){
            throw new IllegalArgumentException("Two emails cannot be the same");
        }

        if (user1 == null || user2 == null){
            throw new IllegalArgumentException("One or both email addresses do not exist");
        }

        if (userRepository.areFriends(user1.getId(), user2.getId())) {
            throw new IllegalArgumentException("Friend connection already exists");
        }

        Friend friend = new Friend();
        friend.setUser(new User(user1.getId()));
        friend.setFriend(new User(user2.getId()));
        friend.setStatus(FriendStatus.FRIEND);
        friendRepository.save(friend);

    }
}
