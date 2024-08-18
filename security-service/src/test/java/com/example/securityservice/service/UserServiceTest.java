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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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
    void testSaveUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testUser");
        userDTO.setPassword("password");
        List<String> roles = List.of("ROLE_USER");
        userDTO.setRoles(roles);

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("encodedpassword");

        Set<UserRole> userRolesSet = roles.stream()
            .map(role -> {
                UserRole userRole = new UserRole();
                userRole.setRole(role);
                userRole.setUser(user);
                return userRole;
            }).collect(Collectors.toSet());

        // Convert Set to List for mock
        List<UserRole> userRolesList = new ArrayList<>(userRolesSet);
        user.setRoles(userRolesSet);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRoleRepository.saveAll(anyList())).thenReturn(userRolesList);

        UserDTO result = userService.saveUser(userDTO);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertNull(result.getPassword());

        // Verify that saveAll was called with a List
        verify(userRepository).save(any(User.class));
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

    @Test
    void testSavePyme() {
        PymeDTO pymeDTO = new PymeDTO();
        pymeDTO.setId(1L);
        pymeDTO.setUsername("testPymeUser");
        pymeDTO.setPassword("password");
        pymeDTO.setPymeName("testPyme");

        User user = new User();
        user.setId(1L);
        user.setUsername("testPymeUser");
        user.setPassword("encodedpassword");
        List<String> roles = List.of("ROLE_USER");
        user.setRoles(roles.stream()
            .map(role -> {
                UserRole userRole = new UserRole();
                userRole.setRole(role);
                userRole.setUser(user);
                return userRole;
            }).collect(Collectors.toSet()));

        pymeDTO.setRoles(user.getRoleNames());

        Pyme pyme = new Pyme();
        pyme.setUser(user);
        pyme.setPymeName("testPyme");

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(pymeRepository.save(any(Pyme.class))).thenReturn(pyme);

        PymeDTO result = userService.savePyme(pymeDTO);

        assertNotNull(result);
        assertEquals("testPymeUser", result.getUsername());
        assertEquals("testPyme", result.getPymeName());
        verify(userRepository).save(any(User.class));
        verify(pymeRepository).save(any(Pyme.class));
    }

}
