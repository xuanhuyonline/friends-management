package com.friends.management.repository;

import com.friends.management.entity.RoleEnum;
import com.friends.management.entity.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RoleRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByName() {
        Role role = new Role();
        role.setName(RoleEnum.ROLE_ADMIN.getRole());
        entityManager.persist(role);
        entityManager.flush();

        Optional<Role> result = roleRepository.findFirstByName(RoleEnum.ROLE_ADMIN.getRole());

        Assertions.assertEquals(role.getName(), result.get().getName());
    }
}
