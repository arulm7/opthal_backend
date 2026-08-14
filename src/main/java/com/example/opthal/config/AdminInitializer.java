package com.example.opthal.config;

import com.example.opthal.model.Role;
import com.example.opthal.model.User;
import com.example.opthal.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!userRepository.existsByEmail("admin@opthal.com")) {

                User admin = new User();

                admin.setName("Admin");
                admin.setEmail("admin@opthal.com");
                admin.setPhone("9999999999");
                admin.setPassword(
                        passwordEncoder.encode("Admin@123")
                );
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println("Admin account created successfully");

            } else {

                System.out.println("Admin account already exists");

            }
        };
    }
}