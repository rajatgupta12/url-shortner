package com.example.urlshortner.service;

import com.example.urlshortner.model.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static com.example.urlshortner.UrlShortenerConstants.*;

@Service
@RequiredArgsConstructor
public class IdGeneratorService {
    private static final long SECRET_MULTIPLIER = 0x9E3779B97F4A7C15L;
    private static final long SECRET_KEY = 0xA5A5A5A5A5A5A5A5L;


    private final MongoTemplate mongoTemplate;
    long current;
    private long max;

    public synchronized Long getNextId() {
        if (current >= max) {
            fetchNextRangeFromDB();
        }

        return obfuscate(++current);
    }

    private Long obfuscate(long id) {
        return (id * SECRET_MULTIPLIER) ^ SECRET_KEY;
    }

    private void fetchNextRangeFromDB() {
        Query query = new Query(Criteria.where(ID).is(COUNTER_ID));
        Update update = new Update().inc(NEXT, RANGE);
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(true).returnNew(true);
        Counter counter = mongoTemplate.findAndModify(query, update, findAndModifyOptions, Counter.class);

        max = counter.getNext();
        current = max - RANGE;
    }
}
