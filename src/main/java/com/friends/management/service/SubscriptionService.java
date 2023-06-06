package com.friends.management.service;

import com.friends.management.common.ApiResponse;
import com.friends.management.dto.SubscriptionRequestDto;
import com.friends.management.entity.Subscription;
import com.friends.management.entity.User;
import com.friends.management.repository.SubscriptionRepository;
import com.friends.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionService implements ISubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    @Transactional
    @Override
    public ApiResponse createUpdateSubscription(SubscriptionRequestDto requestDto) {

        User user1 = userRepository.findByEmail(requestDto.getRequestor());
        User user2 = userRepository.findByEmail(requestDto.getTarget());

        if(user1 == null || user2 == null){
            throw new IllegalArgumentException("One or both email addresses do not exist");
        }

        if(user1 == user2){
            throw new IllegalArgumentException("Two emails cannot be the same");
        }

        if(subscriptionRepository.receivedUpdate(user1.getId(), user2.getId())){
            throw new IllegalArgumentException("Subscription for updates already exists");
        }

        Subscription subscription = new Subscription(user1, user2);
        subscriptionRepository.save(subscription);

        return new ApiResponse(true);
    }
}
