package com.example.aadspringdelegator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Profile("dev")
public class LocalCacheService implements CacheService {
    private final Cache<String, JsonNode> cache;

    private LocalCacheService(CacheManager cacheManager, @Value("${spring.cache.cache-names}") String cacheName) {
        this.cache = (Cache<String, JsonNode>) cacheManager.getCache(cacheName).getNativeCache();
    }

    @Override
    public void put(String key, JsonNode value) {
        cache.put(key, value);
    }

    @Override
    public Mono<JsonNode> get(String key) {
        return Mono.justOrEmpty(cache.getIfPresent(key));
    }
}
