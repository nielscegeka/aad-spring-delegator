package com.example.aadspringdelegator.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class ImageService extends DelegationService{
    @Value("${image.function.url}")
    private String imageUrl;
    @Value("${image.function.code}")
    private String imageCode;

    private final UriBuilder uriBuilder;

    public ImageService(WebClient webClient, UriBuilder uriBuilder) {
        super(webClient);
        this.uriBuilder = uriBuilder;
    }

    public Mono<JsonNode> getImage(String animalName) {
        URI uri = uriBuilder.baseUrl(imageUrl).code(imageCode).animal(animalName).build();
        return super.sendRequest(uri);
    }
}
