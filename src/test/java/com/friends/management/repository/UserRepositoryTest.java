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

import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("test@example.com");
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    public void testFindFriendByEmail() {
        User user1 = new User();
        user1.setEmail("friend1@gmail.com");
        entityManager.persist(user1);

        User user2 = new User();
        user2.setEmail("friend2@gmail.com");
        entityManager.persist(user2);

        Friend friend = new Friend();
        friend.setUser(new User(user1.getId()));
        friend.setFriend(new User(user2.getId()));
        friend.setStatus(FriendStatus.FRIEND);
        entityManager.persist(friend);

        entityManager.flush();

        List<String> friends = userRepository.findFriendByEmail(user1.getId());

        Assertions.assertEquals(friends.size(), 1);
        Assertions.assertEquals(friends.get(0), user2.getEmail());
    }

    @Test
    public void testFindCommonEmails() {
        User user1 = new User();
        user1.setEmail("friend1@gamil.com");
        entityManager.persist(user1);

        User user2 = new User();
        user2.setEmail("friend2@gamil.com");
        entityManager.persist(user2);

        User user3 = new User();
        user3.setEmail("commonfriend@gamil.com");
        entityManager.persist(user3);

        Friend friend1 = new Friend();
        friend1.setUser(new User(user1.getId()));
        friend1.setFriend(new User(user3.getId()));
        friend1.setStatus(FriendStatus.FRIEND);
        entityManager.persist(friend1);

        Friend friend2 = new Friend();
        friend2.setUser(new User(user3.getId()));
        friend2.setFriend(new User(user1.getId()));
        friend2.setStatus(FriendStatus.FRIEND);
        entityManager.persist(friend2);

        Friend friend3 = new Friend();
        friend3.setUser(new User(user2.getId()));
        friend3.setFriend(new User(user3.getId()));
        friend3.setStatus(FriendStatus.FRIEND);
        entityManager.persist(friend3);

        Friend friend4 = new Friend();
        friend4.setUser(new User(user3.getId()));
        friend4.setFriend(new User(user2.getId()));
        friend4.setStatus(FriendStatus.FRIEND);
        entityManager.persist(friend4);

        entityManager.flush();

        List<String> commonFriends = userRepository.findCommonEmails(user1.getId(), user2.getId());

        Assertions.assertEquals(commonFriends.size(), 1);
        Assertions.assertEquals(commonFriends.get(0), user3.getEmail());
    }

    @Test
    public void testFindFriendSubscribedByEmail() {
        User user1 = new User();
        user1.setEmail("friend1@gamil.com");
        entityManager.persist(user1);

        User user2 = new User();
        user2.setEmail("friend2@gamil.com");
        entityManager.persist(user2);

        Friend friend = new Friend();
        friend.setUser(new User(user1.getId()));
        friend.setFriend(new User(user2.getId()));
        friend.setStatus(FriendStatus.FRIEND);
        friend.setSubscriber(true);
        entityManager.persist(friend);

        entityManager.flush();

        List<String> friends = userRepository.findFriendSubscribedByEmail(user2.getId());

        Assertions.assertEquals(friends.size(), 1);
        Assertions.assertEquals(friends.get(0), user1.getEmail());
    }
}