package com.example.servicesapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEnabled(false);
        user.setRoles(new HashSet<>());
    }

    @Test
    public void testUserConstructorWithRoles() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");

        User userWithRoles = new User("testuser", "password", true, roles);

        assertEquals("testuser", userWithRoles.getUsername());
        assertEquals("password", userWithRoles.getPassword());
        assertTrue(userWithRoles.isEnabled());
        assertEquals(roles, userWithRoles.getRoles());
    }

    @Test
    public void testGettersAndSetters() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");

        user.setRoles(roles);
        assertEquals(roles, user.getRoles());

        user.setUsername("newuser");
        assertEquals("newuser", user.getUsername());

        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());

        user.setEnabled(true);
        assertTrue(user.isEnabled());
    }

    @Test
    public void testEmptyRoles() {
        User userWithEmptyRoles = new User("emptyroles", "password", true, new HashSet<>());

        assertTrue(userWithEmptyRoles.getRoles().isEmpty());
    }

    @Test
    public void testNullRoles() {
        User userWithNullRoles = new User("nullroles", "password", true, null);

        assertNull(userWithNullRoles.getRoles());
    }
}
