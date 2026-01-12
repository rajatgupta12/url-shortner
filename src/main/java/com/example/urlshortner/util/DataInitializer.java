package com.example.urlshortner.util;

import com.example.urlshortner.model.Role;
import com.example.urlshortner.model.User;
import com.example.urlshortner.repository.RoleRepository;
import com.example.urlshortner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.example.urlshortner.UrlShortenerConstants.ADMIN;
import static com.example.urlshortner.UrlShortenerConstants.USER;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public static final Map<String, Role> roles = new HashMap<>();

    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = roleRepository.findByName(ADMIN)
                .orElse(new Role(ADMIN));
        Role userRole = roleRepository.findByName(USER)
                        .orElse(new Role(USER));

        roles.put(ADMIN, adminRole);
        roles.put(USER, userRole);

        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            roles.put(ADMIN, roleRepository.save(adminRole));
            roles.put(USER, roleRepository.save(userRole));

            User admin = User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .roles(Set.of(adminRole))
                    .build();
            userRepository.save(admin);
        }
    }
}
