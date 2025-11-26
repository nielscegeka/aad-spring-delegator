package com.example.aadspringdelegator.service;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

public interface CacheService {
    void put(String key, JsonNode value);
    Mono<JsonNode> get(String key);
}
