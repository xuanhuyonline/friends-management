package com.friends.management.repository;

import com.friends.management.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select count(f) > 0 from Friend f " +
            "where (f.user.id = :userId1 and f.friend.id = :userId2 and f.status = 'FRIEND') " +
            "or (f.friend.id = :userId1 and f.user.id = :userId2 and f.status = 'FRIEND')")
    boolean areFriends(Long userId1, Long userId2);

    @Query("select f from Friend f " +
            "where f.user.id = :userId1 and f.friend.id = :userId2")
    Friend friendStatus(Long userId1, Long userId2);

    @Query("select count(f) > 0 from Friend f " +
            "where f.user.id = :userId1 and f.friend.id = :userId2 and f.subscriber = true")
    boolean isReceivedUpdate(Long userId1, Long userId2);

    @Query("select count(f) > 0 from Friend  f " +
            "where (f.user.id = :userId1 and f.friend.id = :userId2 and f.status = 'BLOCK') " +
            "or (f.friend.id = :userId1 and f.user.id = :userId2 and f.status = 'BLOCK')")
    boolean isBlockedEachOther(Long userId1, Long userId2);

}
