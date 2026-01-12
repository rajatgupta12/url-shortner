package com.example.urlshortner.controller;

import com.example.urlshortner.dto.request.AppPropertyRequest;
import com.example.urlshortner.service.AppPropertyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/property")
@RequiredArgsConstructor
@Tag(name = "Application Property")
public class AppPropertyController {

    private final AppPropertyService appPropertyService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<String> createAppProperty(@Valid @RequestBody AppPropertyRequest appPropertyRequest, Authentication authentication) {
        appPropertyService.createProperty(appPropertyRequest, authentication.getName());

        return ResponseEntity.ok("Application Property created successfully");
    }
}
