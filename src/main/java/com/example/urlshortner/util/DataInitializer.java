package com.example.urlshortner.util;

import com.example.urlshortner.model.Role;
import com.example.urlshortner.model.User;
import com.example.urlshortner.repository.RoleRepository;
import com.example.urlshortner.repository.UserRepository;
import com.example.urlshortner.service.CachedPropertyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.example.urlshortner.UrlShortenerConstants.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public static final Map<String, Role> roles = new HashMap<>();

    private final String adminUsername;
    private final String adminPassword;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                           CachedPropertyService cachedPropertyService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = cachedPropertyService.getAppProperty(ADMIN_USERNAME).getValue();
        this.adminPassword = cachedPropertyService.getAppProperty(ADMIN_PASSWORD).getValue();
    }

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
