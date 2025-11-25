package com.example.aadspringdelegator.service;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class UriBuilder {
    private String baseUrl;
    private String code;
    private String animal;

    public UriBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public UriBuilder code(String code) {
        this.code = code;
        return this;
    }

    public UriBuilder animal(String animal) {
        this.animal = animal;
        return this;
    }

    public URI build() {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("code", code)
                .queryParam("animal", animal)
                .encode()
                .build().toUri();
    }
}
