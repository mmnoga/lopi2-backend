package com.liftoff.project.command;

import com.liftoff.project.model.Role;
import com.liftoff.project.model.User;
import com.liftoff.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class UserInsertCommand implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User adminUser = new User();
        adminUser.setFirstName("Admin");
        adminUser.setLastName("Administratorski");
        adminUser.setUsername("admin@example.org");
        adminUser.setPassword(passwordEncoder.encode("lopi2-admin!"));
        adminUser.setRole(Role.ROLE_ADMIN);
        adminUser.setIsEnabled(true);
        adminUser.setUuid(UUID.randomUUID());

        userRepository.save(adminUser);
    }

}