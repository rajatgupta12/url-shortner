package com.example.urlshortner.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UrlMappingResponse {
    private String longUrl;
    private String shortUrl;
}
