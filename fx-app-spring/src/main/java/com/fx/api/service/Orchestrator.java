package com.fx.api.service;

import com.fx.api.model.AckRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class Orchestrator {

    private static final Logger log = LoggerFactory.getLogger(Orchestrator.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public Orchestrator(RestTemplate restTemplate,
            @Value("${fx.orchestrator.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    public void acknowledge(long batchId, String status) {
        try {
            ResponseEntity<Void> ignored = restTemplate.postForEntity(
                    baseUrl + "/api/feed/ack",
                    new AckRequest(batchId, status),
                    Void.class);
            log.info("Acknowledged batch {} as {}", batchId, status);
        } catch (RestClientException ex) {
            log.warn("Failed to acknowledge batch {} as {}: {}", batchId, status, ex.getMessage());
        }
    }
}