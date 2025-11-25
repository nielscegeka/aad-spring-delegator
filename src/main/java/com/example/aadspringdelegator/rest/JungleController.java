package com.example.aadspringdelegator.rest;

import com.example.aadspringdelegator.service.ImageService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequestMapping("/wiki")
@RestController
public class JungleController {
    private final ImageService imageService;

    public JungleController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/image")
    public Mono<JsonNode> getImage(@RequestParam("animal") String animalName) {
       return imageService.getImage(animalName);
    }
}
