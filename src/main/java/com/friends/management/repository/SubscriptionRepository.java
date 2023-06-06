package com.friends.management.repository;

import com.friends.management.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Query("select count(s) > 0 from Subscription s where s.user.id = :userId1 and s.subscriber.id = :userId2")
    boolean receivedUpdate(Long userId1, Long userId2);

}
