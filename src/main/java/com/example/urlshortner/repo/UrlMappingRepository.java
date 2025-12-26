package com.example.urlshortner.repo;

import com.example.urlshortner.model.UrlMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UrlMappingRepository extends MongoRepository<UrlMapping, String> {
}
