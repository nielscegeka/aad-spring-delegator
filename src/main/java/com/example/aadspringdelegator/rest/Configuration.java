package com.example.aadspringdelegator.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Value("${react.frontend.url}")
    private String frontendUrl;

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .codecs(configurer ->
                        configurer.defaultCodecs().maxInMemorySize(5 * 1024 * 1024) // 5MB voor images
                ).build();
    }

    @Bean
    public WebFluxConfigurer webFluxConfigurer() {
        return new WebFluxConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/wiki/**")
                        .allowedOrigins(frontendUrl)
                        .allowedMethods("GET");
            }
        };
    }
}
