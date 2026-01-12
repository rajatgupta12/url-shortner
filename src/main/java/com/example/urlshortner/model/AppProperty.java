package com.example.urlshortner.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("app_properties")
@Builder
@Getter
@Setter
public class AppProperty {
    @Id
    private String key;

    private String value;
    private String createdBy;
    private LocalDateTime createdOn;
}
