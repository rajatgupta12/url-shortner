package com.example.urlshortner.service;

import com.example.urlshortner.model.Role;
import com.example.urlshortner.model.User;
import com.example.urlshortner.repository.RoleRepository;
import com.example.urlshortner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Transactional
    public void assignRole(String username, String roleName) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            log.error("User {} is not found", username);
            throw new RuntimeException("User " + username + " is not found");
        }

        Optional<Role> role = roleRepository.findByName(roleName);
        if (role.isEmpty()) {
            log.error("Role {} is not found", roleName);
            throw new RuntimeException("Role " + roleName + " is not found");
        }

        User user = optionalUser.get();
        user.setRoles(new HashSet<>(Set.of(role.get())));
        userRepository.save(user);
    }
}
