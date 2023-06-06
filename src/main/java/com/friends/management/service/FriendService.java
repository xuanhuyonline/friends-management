package com.friends.management.service;

import com.friends.management.common.ApiResponse;
import com.friends.management.dto.FriendStatus;
import com.friends.management.entity.Friend;
import com.friends.management.entity.User;
import com.friends.management.repository.FriendRepository;
import com.friends.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService implements IFriendService{
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Override
    @Transactional
    public ApiResponse createFriendConnection(List<String> friends) {
        if (friends.size() != 2) {
            throw new IllegalArgumentException("Exactly two email addresses are required");
        }

        User user1 = userRepository.findByEmail(friends.get(0));
        User user2 = userRepository.findByEmail(friends.get(1));

        if (user1 == null || user2 == null){
            throw new IllegalArgumentException("One or both email addresses do not exist");
        }

        if(user1 == user2){
            throw new IllegalArgumentException("Two emails cannot be the same");
        }

        if (userRepository.areFriends(user1.getId(), user2.getId())) {
            throw new IllegalArgumentException("Friend connection already exists");
        }

        List<Friend> myFriends = new ArrayList<>();

        Friend friend1 = new Friend(new User(user1.getId()), new User(user2.getId()), FriendStatus.FRIEND);
        myFriends.add(friend1);

        Friend friend2 = new Friend(new User(user2.getId()), new User(user1.getId()), FriendStatus.FRIEND);
        myFriends.add(friend2);

        friendRepository.saveAll(myFriends);

        return new ApiResponse(true);
    }

    @Override
    public ApiResponse getFriendsList(String email){
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new IllegalArgumentException("Email address does not exist");
        }

        List<String> friends = friendRepository.findFriendsByEmail(user.getId());
        int count = friends.size();

        return new ApiResponse(true,friends,count);
    }

    @Override
    public ApiResponse getCommonFriends(List<String> friends) {
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

        List<String> commonFriends = friendRepository.findCommonEmails(user1.getId(), user2.getId());
        int count = commonFriends.size();
        return new ApiResponse(true,commonFriends,count);
    }


}
