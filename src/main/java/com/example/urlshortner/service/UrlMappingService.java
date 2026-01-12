package com.example.urlshortner.service;

import com.example.urlshortner.dto.response.UrlMappingResponse;
import com.example.urlshortner.exception.UrlShortnerException;
import com.example.urlshortner.model.UrlMapping;
import com.example.urlshortner.repository.UrlMappingRepository;
import com.example.urlshortner.util.Base62Encoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlMappingService {
    private final UrlMappingRepository urlMappingRepository;
    private final IdGeneratorService idGeneratorService;
    private final HttpServletRequest httpServletRequest;

    public UrlMappingResponse createUrlMapping(String longUrl, String username) {
        //Fetch next Id ith Salting
        Long nextId = idGeneratorService.getNextId();

        //Generate ShortURLKey
        Base62Encoder base62Decoder = new Base62Encoder();
        String shortUrlKey = base62Decoder.encode(nextId);

        //Save to DB
        UrlMapping urlMapping = UrlMapping.builder()
                .id(shortUrlKey)
                .longUrl(longUrl)
                .createdBy(username)
                .createdOn(LocalDateTime.now()).build();
        urlMappingRepository.save(urlMapping);

        String shortURL = httpServletRequest.getRequestURL().toString().replace(httpServletRequest.getRequestURI(), "")
                + "/" + shortUrlKey;
        return UrlMappingResponse.builder()
                .shortUrl(shortURL)
                .longUrl(longUrl).build();
    }

    public String getLongUrl(String url) {
        return urlMappingRepository.findById(url)
                .map(UrlMapping::getLongUrl).orElseThrow(() -> {
                    throw new UrlShortnerException(httpServletRequest.getRequestURL() + " not found", HttpStatus.NOT_FOUND);
                });
    }
}
