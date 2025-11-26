package com.example.aadspringdelegator.service;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@Profile("prod")
public class AzureStorageCacheService implements CacheService {
    private final BlobContainerAsyncClient containerClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AzureStorageCacheService(@Value("${azure.storage.connection-string}") String connectionString,
                                    @Value("${azure.storage.container-name}") String containerName) {
        this.containerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildAsyncClient();

        containerClient.exists()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.empty();
                    }
                    return containerClient.create();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    @Override
    public void put(String key, JsonNode value) {
        BlobAsyncClient blobClient = containerClient.getBlobAsyncClient(key);
        blobClient.upload(BinaryData.fromString(value.toString()), true)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    @Override
    public Mono<JsonNode> get(String key) {
        BlobAsyncClient blobClient = containerClient.getBlobAsyncClient(key);
        return blobClient.exists()
                .flatMap(exists -> {
                    if (exists) {
                        return blobClient.downloadContent()
                                .map(BinaryData::toString)
                                .flatMap(json ->
                                    Mono.fromCallable(() -> objectMapper.readTree(json))
                                                                        .onErrorResume(e -> {
                                                                            log.warn("Failed to parse JSON for key {}: {}", key, e.toString());
                                                                            return Mono.empty();
                                                                        })
                                );
                    }
                    return Mono.empty();
                }).onErrorResume(e -> Mono.empty());
    }
}
