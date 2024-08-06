package com.example.securityservice.service;

import com.example.securityservice.dto.UserDTO;
import com.example.securityservice.model.Pyme;
import com.example.securityservice.model.User;
import com.example.securityservice.model.UserRole;
import com.example.securityservice.repository.PymeRepository;
import com.example.securityservice.repository.UserRepository;
import com.example.securityservice.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private PymeRepository pymeRepository;

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public UserDTO save(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        user = userRepository.save(user);

        // Save roles
        Set<UserRole> roles = user.getRoles();
        if (roles != null) {
            final User finalUser = user;
            roles.forEach(role -> role.setUser(finalUser));
            userRoleRepository.saveAll(roles);
        }

        // Save Pyme if the role is PYME
        if (userDTO.getRoles().contains("PYME")) {
            Pyme pyme = pymeRepository.findByUser(user).orElse(new Pyme());
            pyme = updatePymeEntity(pyme, userDTO, user);
            pymeRepository.save(pyme);
        }

        return convertToDto(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(null); // Do not expose password
        userDTO.setDni(user.getDni());
        userDTO.setPostalCode(user.getPostalCode());
        userDTO.setEnabled(user.isEnabled());
        userDTO.setRoles(user.getRoleNames());

        // Set Pyme-specific fields if the user is a Pyme
        user.getRoles().stream()
            .filter(role -> role.getRole().equals("PYME"))
            .findFirst()
            .ifPresent(role -> {
                Pyme pyme = pymeRepository.findByUser(user).orElse(null);
                if (pyme != null) {
                    userDTO.setPymePostalCode(pyme.getPymePostalCode());
                    userDTO.setPymePhone(pyme.getPymePhone());
                    userDTO.setPymeName(pyme.getPymeName());
                    userDTO.setPymeDescription(pyme.getPymeDescription());
                }
            });

        return userDTO;
    }

    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setDni(userDTO.getDni());

        // Parse postalCode to Integer
        user.setPostalCode(userDTO.getPostalCode());

        user.setEnabled(userDTO.isEnabled());

        Set<UserRole> roles = userDTO.getRoles().stream()
                .map(role -> new UserRole(user, role))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return user;
    }

    public Pyme updatePymeEntity(Pyme pyme, UserDTO userDTO, User user) {
        pyme.setUser(user);
        pyme.setPymePostalCode(userDTO.getPymePostalCode());
        pyme.setPymePhone(userDTO.getPymePhone());
        pyme.setPymeName(userDTO.getPymeName());
        pyme.setPymeDescription(userDTO.getPymeDescription());
        return pyme;
    }
}
