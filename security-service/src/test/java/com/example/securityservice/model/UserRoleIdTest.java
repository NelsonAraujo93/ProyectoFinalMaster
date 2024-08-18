package com.example.securityservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserRoleIdTest {

    @Test
    public void testDefaultConstructor() {
        UserRoleId userRoleId = new UserRoleId();
        assertNull(userRoleId.getUserId(), "UserId should be null.");
        assertNull(userRoleId.getRole(), "Role should be null.");
    }

    @Test
    public void testParameterizedConstructor() {
        Long userId = 1L;
        String role = "ROLE_USER";

        UserRoleId userRoleId = new UserRoleId(userId, role);

        assertEquals(userId, userRoleId.getUserId(), "UserId should match.");
        assertEquals(role, userRoleId.getRole(), "Role should match.");
    }

    @Test
    public void testGettersAndSetters() {
        UserRoleId userRoleId = new UserRoleId();
        userRoleId.setUserId(2L);
        userRoleId.setRole("ROLE_ADMIN");

        assertEquals(2L, userRoleId.getUserId(), "UserId should match.");
        assertEquals("ROLE_ADMIN", userRoleId.getRole(), "Role should match.");
    }

    @Test
    public void testEquals() {
        UserRoleId userRoleId1 = new UserRoleId(1L, "ROLE_USER");
        UserRoleId userRoleId2 = new UserRoleId(1L, "ROLE_USER");
        UserRoleId userRoleId3 = new UserRoleId(2L, "ROLE_ADMIN");

        assertEquals(userRoleId1, userRoleId2, "UserRoleId objects with the same values should be equal.");
        assertNotEquals(userRoleId1, userRoleId3, "UserRoleId objects with different values should not be equal.");
        assertNotEquals(userRoleId1, new Object(), "UserRoleId object should not be equal to a different type.");
    }

    @Test
    public void testHashCode() {
        UserRoleId userRoleId1 = new UserRoleId(1L, "ROLE_USER");
        UserRoleId userRoleId2 = new UserRoleId(1L, "ROLE_USER");
        UserRoleId userRoleId3 = new UserRoleId(2L, "ROLE_ADMIN");

        assertEquals(userRoleId1.hashCode(), userRoleId2.hashCode(), "Hash codes should be equal for objects with the same values.");
        assertNotEquals(userRoleId1.hashCode(), userRoleId3.hashCode(), "Hash codes should not be equal for objects with different values.");
    }

    @Test
    public void testEqualsWithEqualObjects() {
        UserRoleId id1 = new UserRoleId(1L, "ROLE_USER");
        UserRoleId id2 = new UserRoleId(1L, "ROLE_USER");

        assertEquals(id1, id2, "UserRoleId instances with the same userId and role should be equal.");
    }

    @Test
    public void testEqualsWithDifferentUserId() {
        UserRoleId id1 = new UserRoleId(1L, "ROLE_USER");
        UserRoleId id2 = new UserRoleId(2L, "ROLE_USER");

        assertNotEquals(id1, id2, "UserRoleId instances with different userId should not be equal.");
    }

    @Test
    public void testEqualsWithDifferentRole() {
        UserRoleId id1 = new UserRoleId(1L, "ROLE_USER");
        UserRoleId id2 = new UserRoleId(1L, "ROLE_ADMIN");

        assertNotEquals(id1, id2, "UserRoleId instances with different roles should not be equal.");
    }

    @Test
    public void testEqualsWithNull() {
        UserRoleId id = new UserRoleId(1L, "ROLE_USER");

        assertNotEquals(id, null, "UserRoleId should not be equal to null.");
    }

    @Test
    public void testEqualsWithDifferentClass() {
        UserRoleId id = new UserRoleId(1L, "ROLE_USER");
        String otherClassObject = "String Object";

        assertNotEquals(id, otherClassObject, "UserRoleId should not be equal to an object of a different class.");
    }

    @Test
    public void testHashCodeConsistency() {
        UserRoleId id1 = new UserRoleId(1L, "ROLE_USER");
        UserRoleId id2 = new UserRoleId(1L, "ROLE_USER");

        assertEquals(id1.hashCode(), id2.hashCode(), "Hash codes should be equal for UserRoleId instances that are equal.");
    }
}
