package com.example.urlshortner.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AppPropertyRequest {
    @NotBlank
    private String key;
    @NotBlank
    private String value;
}
