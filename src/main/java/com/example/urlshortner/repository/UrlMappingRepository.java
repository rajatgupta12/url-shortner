package com.example.urlshortner.repository;

import com.example.urlshortner.model.UrlMapping;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlMappingRepository extends MongoRepository<UrlMapping, String> {
}
