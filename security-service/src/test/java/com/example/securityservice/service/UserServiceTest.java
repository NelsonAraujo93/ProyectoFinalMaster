package com.example.securityservice.service;

import com.example.securityservice.dto.PymeDTO;
import com.example.securityservice.dto.UserDTO;
import com.example.securityservice.model.Pyme;
import com.example.securityservice.model.User;
import com.example.securityservice.model.UserRole;
import com.example.securityservice.repository.PymeRepository;
import com.example.securityservice.repository.UserRepository;
import com.example.securityservice.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private PymeRepository pymeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        Optional<User> result = userService.findByUsername("testUser");

        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
        verify(userRepository).findByUsername("testUser");
    }

    @Test
    void testExistsByUsername() {
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        boolean exists = userService.existsByUsername("existingUser");

        assertTrue(exists);
        verify(userRepository).existsByUsername("existingUser");
    }
}
