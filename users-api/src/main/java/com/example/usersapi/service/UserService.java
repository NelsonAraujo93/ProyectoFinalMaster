package com.example.usersapi.service;

import com.example.usersapi.dto.UserDTO;
import com.example.usersapi.model.User;
import com.example.usersapi.model.UserRole;
import com.example.usersapi.repository.UserRepository;
import com.example.usersapi.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id).map(this::convertToDto);
    }

    public UserDTO save(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId())
                .orElse(new User());
        user.setUsername(userDTO.getUsername() != null ? userDTO.getUsername() : user.getUsername());

        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        user.setEnabled(userDTO.isEnabled());

        Set<UserRole> roles = userDTO.getRoles() != null ? userDTO.getRoles().stream()
                .map(role -> new UserRole(user, role))
                .collect(Collectors.toSet()) : user.getRoles();
        user.setRoles(roles);

        // Ensure roles are properly saved
        if (roles != null) {
            user.setRoles(new HashSet<>()); // Temporarily detach roles to avoid issues
            User savedUser = userRepository.save(user);
            roles.forEach(role -> role.setUser(savedUser)); // Reattach roles to the saved user
            userRoleRepository.saveAll(roles);
            savedUser.setRoles(roles);
            return convertToDto(savedUser);
        } else {
            return convertToDto(userRepository.save(user));
        }
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username).map(this::convertToDto);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEnabled(user.isEnabled());
        userDTO.setRoles(user.getRoleNames() != null ? user.getRoleNames() : List.of()); // Ensure roles is not null
        return userDTO;
    }

    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEnabled(userDTO.isEnabled());

        Set<UserRole> roles = userDTO.getRoles() != null ? userDTO.getRoles().stream()
                .map(role -> new UserRole(user, role))
                .collect(Collectors.toSet()) : new HashSet<>();
        user.setRoles(roles);

        return user;
    }
}
