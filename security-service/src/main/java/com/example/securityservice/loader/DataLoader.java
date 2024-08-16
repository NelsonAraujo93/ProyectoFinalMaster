package com.example.securityservice.loader;

import com.example.securityservice.dto.PymeDTO;
import com.example.securityservice.dto.UserDTO;
import com.example.securityservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create initial Clients
        createClient("client1", "DNI001", 12345, "password1");
        createClient("client2", "DNI002", 12345, "password2");
        createClient("client3", "DNI003", 12345, "password3");
        createClient("client4", "DNI004", 12345, "password4");

        // Create initial Pymes
        createPyme("pyme1", "DNI005", 12345, "password5", "12345", "555-0001", "Pyme 1", "Description for Pyme 1");
        createPyme("pyme2", "DNI006", 12345, "password6", "12345", "555-0002", "Pyme 2", "Description for Pyme 2");
        createPyme("pyme3", "DNI007", 12345, "password7", "12345", "555-0003", "Pyme 3", "Description for Pymepa 3");
        createPyme("pyme4", "DNI008", 12345, "password8", "12345", "555-0004", "Pyme 4", "Description for Pyme 4");
    }

    private void createClient(String username, String dni, int postalCode, String password) {
        if (!userService.existsByUsername(username)) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setDni(dni);
            userDTO.setPostalCode(postalCode);
            userDTO.setPassword(passwordEncoder.encode(password)); // Encode the password
            userDTO.setEnabled(true);
            userDTO.setRoles(Arrays.asList("CLIENT"));
            userService.saveUser(userDTO);
        }
    }

    private void createPyme(String username, String dni, int postalCode, String password,
                            String pymePostalCode, String pymePhone, String pymeName, String pymeDescription) {
        if (!userService.existsByUsername(username)) {
            PymeDTO pymeDTO = new PymeDTO();
            pymeDTO.setUsername(username);
            pymeDTO.setDni(dni);
            pymeDTO.setPostalCode(postalCode);
            pymeDTO.setPassword(passwordEncoder.encode(password)); // Encode the password
            pymeDTO.setEnabled(true);
            pymeDTO.setRoles(Arrays.asList("PYME"));
            pymeDTO.setPymePostalCode(pymePostalCode);
            pymeDTO.setPymePhone(pymePhone);
            pymeDTO.setPymeName(pymeName);
            pymeDTO.setPymeDescription(pymeDescription);
            userService.savePyme(pymeDTO);
        }
    }
}
