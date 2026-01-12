package com.example.urlshortner.service;

import com.example.urlshortner.dto.request.SignupRequest;
import com.example.urlshortner.mapper.UserMapper;
import com.example.urlshortner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void validateAndRegisterUser(final SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.getUserName()).isPresent()) {
            log.error("Username is already in use {}", signupRequest.getUserName());
            throw new RuntimeException("Username is already in use");
        }
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            log.error("Email is already in use {}", signupRequest.getEmail());
            throw new RuntimeException("Email is already in use");
        }

        String hashedPassword = passwordEncoder.encode(signupRequest.getPassword());
        userRepository.save(UserMapper.toEntity(signupRequest, hashedPassword));
        signupRequest.setPassword(null);
    }
}
