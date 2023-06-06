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

        User requestor = userRepository.findByEmail(requestDto.getRequestor());
        User target = userRepository.findByEmail(requestDto.getTarget());

        if(requestor == null || target == null){
            throw new IllegalArgumentException("One or both email addresses do not exist");
        }

        if(requestor == target){
            throw new IllegalArgumentException("Two emails cannot be the same");
        }

        if(subscriptionRepository.receivedUpdate(requestor.getId(), target.getId())){
            throw new IllegalArgumentException("Subscription for updates already exists");
        }

        Subscription subscription = new Subscription(requestor, target);
        subscriptionRepository.save(subscription);

        return new ApiResponse(true);
    }
}
