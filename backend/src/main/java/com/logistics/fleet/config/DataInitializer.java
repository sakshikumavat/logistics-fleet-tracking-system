
package com.logistics.fleet.config;

import com.logistics.fleet.entity.AppUser;
import com.logistics.fleet.repository.AppUserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createTestUser(
            AppUserRepository repository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            String email = "admin@logistics.com";

            if (repository.findByEmail(email).isEmpty()) {
                AppUser user = new AppUser();

                user.setUserId(1L);
                user.setFullName("Logistics Admin");
                user.setEmail(email);
                user.setPasswordHash(
                        passwordEncoder.encode("Admin@12345"));
                user.setRole("ADMIN");
                user.setActive(1);
                user.setCreatedAt(LocalDateTime.now());

                repository.save(user);

                System.out.println("Test admin user created.");
            }
        };
    }
}
