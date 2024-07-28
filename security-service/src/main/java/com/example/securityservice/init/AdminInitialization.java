package com.example.securityservice.init;

import com.example.securityservice.model.User;
import com.example.securityservice.model.UserRole;
import com.example.securityservice.model.UserRoleId;
import com.example.securityservice.repository.UserRepository;
import com.example.securityservice.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitialization implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("adminpassword"));
            admin.setEnabled(true);

            User savedAdmin = userRepository.save(admin);

            UserRole adminRole = new UserRole();
            UserRoleId adminRoleId = new UserRoleId();
            adminRoleId.setUserId(savedAdmin.getId());
            adminRoleId.setRole("ADMIN");
            adminRole.setId(adminRoleId);
            adminRole.setUser(savedAdmin);

            userRoleRepository.save(adminRole);
        }
    }
}
