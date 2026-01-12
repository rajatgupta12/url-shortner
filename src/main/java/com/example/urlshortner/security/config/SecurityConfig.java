package com.example.urlshortner.security.config;

import com.example.urlshortner.security.exception.CustomAccessDeniedHandler;
import com.example.urlshortner.security.exception.CustomAuthenticationEntryPoint;
import com.example.urlshortner.security.filter.JwtAuthenticationFilter;
import com.example.urlshortner.service.CachedPropertyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.urlshortner.UrlShortenerConstants.*;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final String unprotectedPaths;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter,  CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                           CustomAccessDeniedHandler customAccessDeniedHandler, CachedPropertyService cachedPropertyService) {
        this.jwtFilter = jwtFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.unprotectedPaths = cachedPropertyService.getAppProperty(UNPROTECTED_PATHS).getValue();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/users/*/roles").hasRole("ADMIN")
                        .requestMatchers(unprotectedPaths.split(",")).permitAll()
                        .requestMatchers(HttpMethod.GET, "/{url}").permitAll()
                        .anyRequest().authenticated())
                .headers(headerConfigurer -> headerConfigurer.
                        contentSecurityPolicy(csp -> csp.policyDirectives("script-src 'self'")))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))
                .build();
    }
}
