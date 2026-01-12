package com.example.urlshortner.controller;

import com.example.urlshortner.dto.response.BaseResponse;
import com.example.urlshortner.dto.response.UrlMappingResponse;
import com.example.urlshortner.dto.request.UrlRequest;
import com.example.urlshortner.exception.UrlShortnerException;
import com.example.urlshortner.service.UrlMappingService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UrlMappingController {

    private final UrlMappingService urlMappingService;

    @PostMapping("/url")
    @RateLimiter(name = "apiLimiter", fallbackMethod = "rateLimitFallback")
    public BaseResponse<UrlMappingResponse> createURLMapping(@RequestBody UrlRequest urlRequest, Authentication authentication) {
        UrlMappingResponse response = urlMappingService.createUrlMapping(urlRequest.getUrl(), authentication.getName());
        return new BaseResponse<>(response);
    }

    public BaseResponse<UrlMappingResponse> rateLimitFallback(UrlRequest urlRequest, Authentication authentication, RequestNotPermitted ex) {
        throw new UrlShortnerException("Too many requests", HttpStatus.TOO_MANY_REQUESTS);
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
