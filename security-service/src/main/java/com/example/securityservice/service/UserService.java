package com.example.securityservice.service;

import com.example.securityservice.dto.UserDTO;
import com.example.securityservice.model.User;
import com.example.securityservice.model.UserRole;
import com.example.securityservice.repository.UserRepository;
import com.example.securityservice.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public UserDTO save(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        // Ensure roles are properly saved
        Set<UserRole> roles = user.getRoles();
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

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEnabled(user.isEnabled());
        userDTO.setRoles(user.getRoleNames()); // Convert roles to List<String>
        return userDTO;
    }

    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEnabled(userDTO.isEnabled());

        Set<UserRole> roles = userDTO.getRoles().stream()
                .map(role -> new UserRole(user, role))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return user;
    }
}
