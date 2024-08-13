package com.example.securityservice.service;

import com.example.securityservice.dto.PymeDTO;
import com.example.securityservice.dto.UserDTO;
import com.example.securityservice.model.Pyme;
import com.example.securityservice.model.User;
import com.example.securityservice.model.UserRole;
import com.example.securityservice.repository.PymeRepository;
import com.example.securityservice.repository.UserRepository;
import com.example.securityservice.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public UserDTO saveUser(UserDTO userDTO) {
        User user = convertToUserEntity(userDTO);
        user = userRepository.save(user);

        saveUserRoles(user);

        return convertToUserDto(user);
    }

    public PymeDTO savePyme(PymeDTO pymeDTO) {
        // Create User from PymeDTO
        User user = convertToUserEntityFromPyme(pymeDTO);
        user = userRepository.save(user); // Save user first to get the ID

        // Save the user's roles
        saveUserRoles(user);

        // Create and save Pyme
        Pyme pyme = new Pyme();
        pyme.setUser(user); // Establish relationship with the user
        pyme.setPymePostalCode(pymeDTO.getPymePostalCode());
        pyme.setPymePhone(pymeDTO.getPymePhone());
        pyme.setPymeName(pymeDTO.getPymeName());
        pyme.setPymeDescription(pymeDTO.getPymeDescription());

        pyme = pymeRepository.save(pyme); // Save Pyme

        return convertToPymeDto(user, pyme);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // Conversion methods
    public UserDTO convertToUserDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(null); // Do not expose password
        userDTO.setDni(user.getDni());
        userDTO.setPostalCode(user.getPostalCode());
        userDTO.setEnabled(user.isEnabled());
        userDTO.setRoles(user.getRoleNames());

        return userDTO;
    }

    public PymeDTO convertToPymeDto(User user, Pyme pyme) {
        PymeDTO pymeDTO = new PymeDTO();
        pymeDTO.setId(user.getId());
        pymeDTO.setUsername(user.getUsername());
        pymeDTO.setPassword(null); // Do not expose password
        pymeDTO.setDni(user.getDni());
        pymeDTO.setPostalCode(user.getPostalCode());
        pymeDTO.setEnabled(user.isEnabled());
        pymeDTO.setRoles(user.getRoleNames());

        pymeDTO.setPymePostalCode(pyme.getPymePostalCode());
        pymeDTO.setPymePhone(pyme.getPymePhone());
        pymeDTO.setPymeName(pyme.getPymeName());
        pymeDTO.setPymeDescription(pyme.getPymeDescription());

        return pymeDTO;
    }

    public User convertToUserEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setDni(userDTO.getDni());
        user.setPostalCode(userDTO.getPostalCode());
        user.setEnabled(userDTO.isEnabled());

        Set<UserRole> roles = userDTO.getRoles().stream()
                .map(role -> new UserRole(user, role))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return user;
    }

    public User convertToUserEntityFromPyme(PymeDTO pymeDTO) {
        User user = new User();
        user.setId(pymeDTO.getId());
        user.setUsername(pymeDTO.getUsername());
        user.setPassword(pymeDTO.getPassword());
        user.setDni(pymeDTO.getDni());
        user.setPostalCode(pymeDTO.getPostalCode());
        user.setEnabled(pymeDTO.isEnabled());

        Set<UserRole> roles = pymeDTO.getRoles().stream()
                .map(role -> new UserRole(user, role))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return user;
    }

    private void saveUserRoles(User user) {
        Set<UserRole> roles = user.getRoles();
        if (roles != null) {
            roles.forEach(role -> role.setUser(user));
            userRoleRepository.saveAll(roles);
        }
    }
}
