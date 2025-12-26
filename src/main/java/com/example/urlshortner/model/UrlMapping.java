package com.example.urlshortner.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("url_mappings")
@Getter
@Setter
@Builder
public class UrlMapping {
    @Id
    private String id;

    private String longUrl;

    private LocalDateTime createdOn;
}
