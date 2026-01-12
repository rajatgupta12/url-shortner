package com.example.urlshortner.service;

import com.example.urlshortner.model.AppProperty;
import com.example.urlshortner.repository.AppPropertyRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CachedPropertyService {
    private final AppPropertyRepository appPropertyRepository;

    Map<String, AppProperty> appPropertiesMap = new HashMap<>();

    @PostConstruct
    public void init() {
        refresh();
    }

    @Scheduled(fixedRate = 3600000)
    public void refresh() {
        appPropertiesMap.clear();
        log.info("Refreshing cached properties");

        appPropertyRepository.findAll()
                .forEach(appProperty -> appPropertiesMap.put(appProperty.getKey(), appProperty));
    }

    public  AppProperty getAppProperty(String appPropertyKey) {
        return appPropertiesMap.get(appPropertyKey);
    }
}
