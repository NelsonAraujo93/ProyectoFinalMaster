package com.example.securityservice.model;

import com.example.securityservice.repository.UserRepository;
import com.example.securityservice.repository.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class UserRoleTest {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUserRole() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setDni("123456789");
        user.setPostalCode(12345);
        userRepository.save(user);

        UserRole userRole = new UserRole();
        userRole.setRole("ROLE_USER");
        userRole.setUser(user);
        UserRole savedUserRole = userRoleRepository.save(userRole);

        assertNotNull(savedUserRole.getId(), "UserRole ID should not be null after saving.");
        assertEquals("ROLE_USER", savedUserRole.getRole(), "Roles should match.");
        assertEquals(user.getId(), savedUserRole.getUser().getId(), "User IDs should match.");
    }

    @Test
    public void testUserRoleConstructor() {
        User user = new User();
        user.setId(1L);
        UserRole role = new UserRole(user, "ROLE_USER");
        
        assertNotNull(role.getId(), "UserRoleId should be initialized.");
        assertEquals(user.getId(), role.getId().getUserId(), "User ID in UserRoleId should match.");
        assertEquals("ROLE_USER", role.getId().getRole(), "Role in UserRoleId should match.");
    }

    @Test
    public void testSetId() {
        User user = new User();
        UserRoleId newId = new UserRoleId(2L, "ROLE_ADMIN");
        UserRole role = new UserRole(user, "ROLE_USER");
        role.setId(newId);

        assertEquals(newId, role.getId(), "UserRoleId should be updated.");
    }

    @Test
    public void testSetUser() {
        User user1 = new User();
        user1.setId(1L);
        UserRole role = new UserRole(user1, "ROLE_USER");

        User user2 = new User();
        user2.setId(2L);
        role.setUser(user2);

        assertEquals(user2.getId(), role.getId().getUserId(), "User ID in UserRoleId should be updated.");
        assertEquals("ROLE_USER", role.getId().getRole(), "Role in UserRoleId should remain unchanged.");
    }

    @Test
    public void testSetUserWhenIdIsNull() {
        // Arrange
        User user = new User();
        user.setId(1L); // Set some ID for the user
        UserRole role = new UserRole(); // No UserRoleId initially

        // Act
        role.setUser(user);

        // Assert
        assertNotNull(role.getId(), "UserRoleId should be initialized when user is set.");
        assertEquals(user.getId(), role.getId().getUserId(), "User ID should be set in UserRoleId.");
    }

    @Test
    public void testSetUserWhenIdIsNotNull() {
        // Arrange
        User user = new User();
        user.setId(1L);
        UserRoleId existingId = new UserRoleId(2L, "ROLE_USER");
        UserRole role = new UserRole();
        role.setId(existingId); // Set existing UserRoleId

        // Act
        role.setUser(user);

        // Assert
        assertEquals(existingId, role.getId(), "UserRoleId should not be changed when already set.");
        assertEquals(user.getId(), role.getId().getUserId(), "User ID should be updated in UserRoleId.");
    }
}
