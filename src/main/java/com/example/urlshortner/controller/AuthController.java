package com.example.urlshortner.controller;

import com.example.urlshortner.dto.request.LoginRequest;
import com.example.urlshortner.dto.request.LogoutRequest;
import com.example.urlshortner.dto.request.RefreshRequest;
import com.example.urlshortner.dto.request.SignupRequest;
import com.example.urlshortner.dto.response.AccessTokenResponse;
import com.example.urlshortner.dto.response.LoginResponse;
import com.example.urlshortner.model.RefreshToken;
import com.example.urlshortner.service.AuthService;
import com.example.urlshortner.service.CustomUserDetailsService;
import com.example.urlshortner.service.JwtService;
import com.example.urlshortner.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "authentication")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody final SignupRequest signupRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.info("{} {}", fieldError.getField(), fieldError.getDefaultMessage());
            }
        }
        try {
            authService.validateAndRegisterUser(signupRequest);
        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ex.getMessage());
        }

        return ResponseEntity.ok("User registered successfully");
    }

    /* Sending tokens in response */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        String accessToken = jwtService.generateToken(authenticate.getName(), authenticate.getAuthorities());
        String refreshToken = refreshTokenService.createRefreshToken(loginRequest.getUsername());
        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
        RefreshToken refreshToken = refreshTokenService.validate(refreshRequest.getToken());
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(refreshToken.getUsername());

        String newAccessToken = jwtService.generateToken(refreshToken.getUsername(), userDetails.getAuthorities());
        return ResponseEntity.ok(new AccessTokenResponse(newAccessToken));
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequest logoutRequest) {
        refreshTokenService.revokeToken(logoutRequest.getRefreshToken());
    }
}