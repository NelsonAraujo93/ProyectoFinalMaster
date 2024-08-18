package com.example.servicesapi.service;

import com.example.servicesapi.model.User;
import com.example.servicesapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("user1");
        
        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
        
        verify(userRepository).findAll();
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1);
        user.setUsername("user");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("user", result.get().getUsername());
        
        verify(userRepository).findById(anyLong());
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("newUser");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("updatedUser");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(user);

        assertNotNull(result);
        assertEquals("updatedUser", result.getUsername());
        
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(anyLong());

        userService.deleteUser(1L);

        verify(userRepository).deleteById(anyLong());
    }

    @Test
    void testGetAllClients() {
        User client1 = new User();
        client1.setId(1);
        client1.setUsername("client1");

        Set<String> roles = Set.of("CLIENT");
        client1.setRoles(roles);

        User client2 = new User();
        client2.setId(2);
        client2.setUsername("client2");
        client2.setRoles(roles);

        List<User> clients = Arrays.asList(client1, client2);

        when(userRepository.findByRolesContaining("CLIENT")).thenReturn(clients);

        List<User> result = userService.getAllClients();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("client1", result.get(0).getUsername());
        assertEquals("client2", result.get(1).getUsername());
        
        verify(userRepository).findByRolesContaining("CLIENT");
    }
}
