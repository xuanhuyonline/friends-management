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

    @Query(value = "SELECT Users.email FROM Friends JOIN Users ON Friends.friend_id = Users.id " +
            "WHERE Friends.user_id = :userId and friends.status = 'FRIEND'", nativeQuery = true)
    List<String> findFriendByEmail(Long userId);

    @Query(value = "SELECT u.email FROM users u " +
            "JOIN friends f1 ON u.id = f1.user_id " +
            "JOIN friends f2 ON u.id = f2.friend_id " +
            "JOIN users u1 ON f1.friend_id = u1.id " +
            "JOIN users u2 ON f2.user_id = u2.id " +
            "WHERE u1.id = :userId1 AND u2.id = :userId2", nativeQuery = true)
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
