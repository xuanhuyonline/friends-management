package com.friends.management.repository;

import com.friends.management.dto.FriendStatus;
import com.friends.management.entity.Friend;
import com.friends.management.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class FriendRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FriendRepository friendRepository;

    @Test
    public void testIsAlreadyFriends() {
        User user1 = new User();
        user1.setEmail("friend1@gmail.com");
        entityManager.persist(user1);

        User user2 = new User();
        user2.setEmail("friend2@gmail.com");
        entityManager.persist(user2);

        Friend friend1 = new Friend();
        friend1.setUser(new User(user1.getId()));
        friend1.setFriend(new User(user2.getId()));
        friend1.setStatus(FriendStatus.FRIEND);
        entityManager.persist(friend1);

        Friend friend2 = new Friend();
        friend2.setUser(new User(user2.getId()));
        friend2.setFriend(new User(user1.getId()));
        friend2.setStatus(FriendStatus.FRIEND);
        entityManager.persist(friend2);

        entityManager.flush();

        boolean result = friendRepository.isAlreadyFriends(user1.getId(), user2.getId());
        Assertions.assertTrue(result);
    }

    @Test
    public void testFriendStatus() {
        User user1 = new User();
        user1.setEmail("friend1@gmail.com");
        entityManager.persist(user1);

        User user2 = new User();
        user2.setEmail("friend2@gmail.com");
        entityManager.persist(user2);

        Friend friend1 = new Friend();
        friend1.setUser(new User(user1.getId()));
        friend1.setFriend(new User(user2.getId()));
        entityManager.persist(friend1);

        entityManager.flush();

        Friend friendStatus = friendRepository.friendStatus(user1.getId(), user2.getId());
        Assertions.assertEquals(friendStatus.getUser().getId(), user1.getId());
        Assertions.assertEquals(friendStatus.getFriend().getId(), user2.getId());
    }

    @Test
    public void testIsReceivedUpdate() {
        User user1 = new User();
        user1.setEmail("friend1@gmail.com");
        entityManager.persist(user1);

        User user2 = new User();
        user2.setEmail("friend2@gmail.com");
        entityManager.persist(user2);

        Friend friend1 = new Friend();
        friend1.setUser(new User(user1.getId()));
        friend1.setFriend(new User(user2.getId()));
        friend1.setSubscriber(true);
        entityManager.persist(friend1);

        entityManager.flush();

        boolean result = friendRepository.isReceivedUpdate(user1.getId(), user2.getId());
        Assertions.assertTrue(result);
    }

    @Test
    public void testIsBlockedEachOther() {
        User user1 = new User();
        user1.setEmail("friend1@gmail.com");
        entityManager.persist(user1);

        User user2 = new User();
        user2.setEmail("friend2@gmail.com");
        entityManager.persist(user2);

        Friend friend1 = new Friend();
        friend1.setUser(new User(user1.getId()));
        friend1.setFriend(new User(user2.getId()));
        friend1.setStatus(FriendStatus.BLOCK);
        entityManager.persist(friend1);

        entityManager.flush();

        boolean result = friendRepository.isBlockedEachOther(user1.getId(), user2.getId());
        Assertions.assertTrue(result);
    }
}
