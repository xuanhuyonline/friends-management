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

    @Query("select u.email from User u join Friend f on u.id = f.user.id " +
            "where (f.friend.id = :userId1 or f.friend.id = :userId2) and f.status = 'FRIEND' " +
            "group by u.email " +
            "having count(u.email) >1")
    List<String> findCommonEmails(Long userId1, Long userId2);

    @Query("select f from Friend f " +
            "where f.user.id = :userId1 and f.friend.id = :userId2")
    Friend friendStatus(Long userId1, Long userId2);

    @Query("select count(f) > 0 from Friend f " +
            "where f.user.id = :userId1 and f.friend.id = :userId2 and f.subscriber = true")
    boolean receivedUpdate(Long userId1, Long userId2);

    @Query("select count(f) > 0 from Friend  f " +
            "where (f.user.id = :userId1 and f.friend.id = :userId2 and f.status = 'BLOCK') " +
            "or (f.friend.id = :userId1 and f.user.id = :userId2 and f.status = 'BLOCK')")
    boolean blockedEachOther(Long userId1, Long userId2);

    @Query("select u.email from Friend f " +
            "join User u on f.user.id = u.id " +
            "where f.friend.id = :userId and f.status = 'FRIEND' and f.subscriber = true ")
    List<String> findFriendSubscribedByEmail(Long userId);


}
