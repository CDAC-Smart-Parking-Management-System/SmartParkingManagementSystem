package com.smartparking.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.smartparking.entity.Role;
import com.smartparking.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        createRole("ADMIN");
        createRole("CUSTOMER");
        createRole("ATTENDANT");
    }

    private void createRole(String roleName) {

        if (!roleRepository.existsByRoleName(roleName)) {

            Role role = new Role();
            role.setRoleName(roleName);

            roleRepository.save(role);
        }
    }

}