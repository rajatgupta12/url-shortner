package com.example.urlshortner.security.auth.provider;

import com.example.urlshortner.security.auth.token.JwtAuthenticationToken;
import com.example.urlshortner.security.userDetails.JwtUserDetails;
import com.example.urlshortner.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        JwtUserDetails userDetails = jwtService.validateJwtToken(token);

        if (userDetails == null)   throw new BadCredentialsException("Invalid token");

        log.info("Logged In User {}", userDetails.getUsername());

        return new JwtAuthenticationToken(userDetails, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
