package com.example.securityservice.model;

import com.example.securityservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setDni("123456789");
        user.setPostalCode(12345);

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId(), "User ID should not be null after saving.");
        assertEquals("testuser", savedUser.getUsername(), "Usernames should match.");
    }

    @Test
    public void testFindByUsername() {
        User user = new User();
        user.setUsername("uniqueuser");
        user.setPassword("password");
        user.setDni("987654321");
        user.setPostalCode(54321);

        userRepository.save(user);

        User foundUser = userRepository.findByUsername("uniqueuser");

        assertNotNull(foundUser, "User should be found by username.");
        assertEquals("uniqueuser", foundUser.getUsername(), "Usernames should match.");
    }

      @Test
    public void testUserRoles() {
        User user = new User();
        user.setUsername("roleuser");
        user.setPassword("password");
        user.setDni("111111111");
        user.setPostalCode(11111);

        // Create and assign roles if UserRole is a valid entity
        UserRole role = new UserRole();
        role.setRole("ROLE_USER");
        role.setUser(user);

        Set<UserRole> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getRoles(), "User roles should not be null.");
        assertTrue(savedUser.getRoles().size() > 0, "User should have at least one role.");
        assertEquals("ROLE_USER", savedUser.getRoleNames().get(0), "Role names should match.");
    }

    @Test
    public void testUserPymeAssociation() {
        User user = new User();
        user.setUsername("pymeuser");
        user.setPassword("password");
        user.setDni("222222222");
        user.setPostalCode(22222);

        Pyme pyme = new Pyme();
        pyme.setPymeDescription("Test Description");
        pyme.setPymeName("Test Name");
        pyme.setPymePhone("123456789");
        pyme.setPymePostalCode("33333");
        pyme.setUser(user);

        user.setPyme(pyme);

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getPyme(), "User should have an associated Pyme.");
        assertEquals("Test Description", savedUser.getPyme().getPymeDescription(), "Pyme description should match.");
        assertEquals("33333", savedUser.getPyme().getPymePostalCode(), "Pyme postal code should match.");
    }

    @Test
    public void testGetRoleNames() {
        User user = new User();
        user.setUsername("roleNamesUser");
        user.setPassword("password");
        user.setDni("333333333");
        user.setPostalCode(33333);

        // Create and assign roles
        UserRole role1 = new UserRole();
        role1.setRole("ROLE_USER");
        role1.setUser(user);

        UserRole role2 = new UserRole();
        role2.setRole("ROLE_ADMIN");
        role2.setUser(user);

        Set<UserRole> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        List<String> roleNames = savedUser.getRoleNames();

        assertNotNull(roleNames, "Role names should not be null.");
        assertTrue(roleNames.contains("ROLE_USER"), "Role names should include ROLE_USER.");
        assertTrue(roleNames.contains("ROLE_ADMIN"), "Role names should include ROLE_ADMIN.");
    }

    @Test
    public void testGettersAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setDni("444444444");
        user.setPostalCode(44444);
        user.setEnabled(true);
        user.setPassword("password");

        assertEquals("password", user.getPassword(), "Password should match.");
        assertEquals(1L, user.getId(), "User ID should match.");
        assertEquals("444444444", user.getDni(), "DNI should match.");
        assertEquals(44444, user.getPostalCode(), "Postal code should match.");
        assertTrue(user.isEnabled(), "User should be enabled.");
        
        user.setEnabled(false);
        assertFalse(user.isEnabled(), "User should be disabled after calling setEnabled(false).");
    }
    
}
