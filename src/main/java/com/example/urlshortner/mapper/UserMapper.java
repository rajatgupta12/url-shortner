package com.example.urlshortner.mapper;

import com.example.urlshortner.dto.request.SignupRequest;
import com.example.urlshortner.model.User;
import com.example.urlshortner.util.DataInitializer;

import java.util.HashSet;
import java.util.Set;

import static com.example.urlshortner.UrlShortenerConstants.USER;

public class UserMapper {

    public static User toEntity(SignupRequest signupRequest, String hashPassword) {
        return User.builder()
                .username(signupRequest.getUserName())
                .password(hashPassword)
                .email(signupRequest.getEmail())
                .roles(new HashSet<>(Set.of(DataInitializer.roles.get(USER))))
                .build();
    }
}
