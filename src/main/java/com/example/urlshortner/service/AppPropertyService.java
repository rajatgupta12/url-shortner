package com.example.urlshortner.service;

import com.example.urlshortner.dto.request.AppPropertyRequest;
import com.example.urlshortner.model.AppProperty;
import com.example.urlshortner.repository.AppPropertyRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AppPropertyService {

    private final AppPropertyRepository appPropertyRepository;

    @Transactional
    public void createProperty(@Valid AppPropertyRequest appPropertyRequest, String username) {
        AppProperty appProperty = AppProperty.builder()
                .key(appPropertyRequest.getKey())
                .value(appPropertyRequest.getValue())
                .createdBy(username)
                .createdOn(LocalDateTime.now())
                .build();

        appPropertyRepository.save(appProperty);
    }
}
