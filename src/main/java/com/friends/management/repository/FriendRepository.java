package com.friends.management.repository;

import com.friends.management.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select count(f) > 0 from Friend f " +
            "where (f.user.id = :userId1 and f.friend.id = :userId2 and f.status = 'FRIEND') " +
            "or (f.friend.id = :userId1 and f.user.id = :userId2 and f.status = 'FRIEND')")
    boolean areFriends(Long userId1, Long userId2);

    @Query("select u.email from Friend f join User u on f.friend.id = u.id " +
            "where f.user.id = :userId and f.status = 'FRIEND'")
    List<String> findFriendByEmail(Long userId);

    @Query("select u.email from User u " +
            "join Friend f1 on u.id = f1.user.id " +
            "join Friend f2 on u.id = f2.friend.id " +
            "join User u1 on f1.friend.id = u1.id " +
            "join User u2 on f2.user.id =u2.id " +
            "where  u1.id = :userId1 and u2.id = :userId2 and f1.status = 'FRIEND' and f2.status = 'FRIEND'")
    List<String> findCommonEmails(Long userId1, Long userId2);

    @Query("select f from Friend f " +
            "where f.user.id = :userId1 and f.friend.id = :userId2")
    Friend friendStatus(Long userId1, Long userId2);

    @Query("select count(f) > 0 from Friend f " +
            "where f.user.id = :userId1 and f.friend.id = :userId2 and f.subscriber = true")
    boolean receivedUpdate(Long userId1, Long userId2);

    @Query("select count(f) > 0 from Friend  f " +
            "where (f.user.id = :userId1 and f.friend.id = :userId2 and f.status = 'BLOCK') or (f.friend.id = :userId1 and f.user.id = :userId2 and f.status = 'BLOCK')")
    boolean blockedEachOther(Long userId1, Long userId2);


}
