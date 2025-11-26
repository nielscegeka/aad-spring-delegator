package com.example.aadspringdelegator.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Service
public class ImageService extends DelegationService{
    @Value("${image.function.url}")
    private String imageUrl;
    @Value("${image.function.code}")
    private String imageCode;

    private final UriBuilder uriBuilder;
    private final CacheService cacheService;

    public ImageService(WebClient webClient, UriBuilder uriBuilder, CacheService cacheService) {
        super(webClient);
        this.uriBuilder = uriBuilder;
        this.cacheService = cacheService;
    }

    public Mono<JsonNode> getImage(String animalName) {
        return cacheService.get(animalName)
                .flatMap(cached -> {
                    log.info("Returning cached image for animal {}", animalName);
                    return Mono.just(cached);
                }).switchIfEmpty(
                    getImageFromFunction(animalName)
                            .flatMap(image -> {
                                cacheService.put(animalName, image);
                                log.info("Returning new image for animal {}", animalName);
                                return Mono.just(image);
                            })
                );
    }

    private Mono<JsonNode> getImageFromFunction(String animalName) {
        URI uri = uriBuilder.baseUrl(imageUrl).code(imageCode).animal(animalName).build();
        return super.sendRequest(uri);
    }
}
