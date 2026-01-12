package com.example.urlshortner.repository;

import com.example.urlshortner.model.AppProperty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppPropertyRepository extends MongoRepository<AppProperty, String> {
}
