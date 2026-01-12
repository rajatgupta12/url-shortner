package com.example.urlshortner.controller;

import com.example.urlshortner.dto.response.BaseResponse;
import com.example.urlshortner.dto.response.UrlMappingResponse;
import com.example.urlshortner.dto.request.UrlRequest;
import com.example.urlshortner.service.UrlMappingService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UrlMappingController {

    private final UrlMappingService urlMappingService;

    @PostMapping("/url")
    public BaseResponse<UrlMappingResponse> createURLMapping(@RequestBody UrlRequest urlRequest) {
        UrlMappingResponse response = urlMappingService.createUrlMapping(urlRequest.getUrl());
        return new BaseResponse<>(response);
    }

    @Hidden
    @GetMapping("/{url}")
    public ResponseEntity<Void> getURLMapping(@PathVariable String url) {
        String longUrl = urlMappingService.getLongUrl(url);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }
}
