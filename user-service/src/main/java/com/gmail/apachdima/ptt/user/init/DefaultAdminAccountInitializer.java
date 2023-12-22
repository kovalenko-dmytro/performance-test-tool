package com.gmail.apachdima.ptt.user.init;

import com.gmail.apachdima.ptt.user.model.User;
import com.gmail.apachdima.ptt.user.model.UserRole;
import com.gmail.apachdima.ptt.user.repository.RoleRepository;
import com.gmail.apachdima.ptt.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class DefaultAdminAccountInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String DEFAULT_ADMIN_NAME = "default-admin";

    @Value("${application.default-admin.username}")
    private String username;
    @Value("${application.default-admin.password}")
    private String password;
    @Value("${application.default-admin.email}")
    private String email;

    @Override
    public void run(String... args) {
        Optional<User> optionalAdmin = userRepository.findByUsername(username);
        if (optionalAdmin.isEmpty()) {
            User defaultAdmin = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password.trim()))
                .firstName(DEFAULT_ADMIN_NAME)
                .lastName(DEFAULT_ADMIN_NAME)
                .email(email)
                .enabled(true)
                .created(LocalDateTime.now())
                .roles(Set.of(roleRepository.findByRole(UserRole.ADMIN).get()))
                .build();
            userRepository.save(defaultAdmin);
        }
    }
}
