package com.friends.management.repository;

import com.friends.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.email = :email")
    User findByEmail(String email);
    @Query("select count(f) > 0 from Friend f where (f.user.id = :userId1 and f.friend.id = :userId2 and f.status = 'FRIEND') or (f.friend.id = :userId1 and f.user.id = :userId2 and f.status = 'FRIEND')")
    boolean areFriends(Long userId1, Long userId2);

}
