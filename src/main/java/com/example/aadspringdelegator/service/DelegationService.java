package com.example.aadspringdelegator.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class DelegationService {
    private final WebClient webClient;

    public DelegationService(WebClient webClient) {
        this.webClient = webClient;
    }

    protected Mono<JsonNode> sendRequest(URI uri) {
        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .cache();
    }
}
