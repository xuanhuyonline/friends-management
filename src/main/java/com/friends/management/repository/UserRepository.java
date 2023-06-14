package com.friends.management.repository;

import com.friends.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("select u.email from Friend f join User u on f.friend.id = u.id " +
            "where f.user.id = :userId and f.status = 'FRIEND'")
    List<String> findFriendByEmail(Long userId);

    @Query("select u.email from User u join Friend f on u.id = f.user.id " +
            "where (f.friend.id = :userId1 or f.friend.id = :userId2) and f.status = 'FRIEND' " +
            "group by u.email " +
            "having count(u.email) >1")
    List<String> findCommonEmails(Long userId1, Long userId2);

    @Query("select u.email from Friend f " +
            "join User u on f.user.id = u.id " +
            "where f.friend.id = :userId and f.status = 'FRIEND' and f.subscriber = true ")
    List<String> findFriendSubscribedByEmail(Long userId);

}
