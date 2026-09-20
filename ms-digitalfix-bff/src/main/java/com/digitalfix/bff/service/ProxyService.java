package com.digitalfix.bff.service;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ProxyService {
    private final RestClient restClient = RestClient.create();

    public ResponseEntity<byte[]> forward(
            String targetUrl,
            HttpMethod method,
            String authorization,
            String contentType,
            byte[] body) {

        return restClient.method(method)
                .uri(URI.create(targetUrl))
                .headers(headers -> {
                    if (authorization != null && !authorization.isBlank()) {
                        headers.set(HttpHeaders.AUTHORIZATION, authorization);
                    }
                    if (contentType != null && !contentType.isBlank()) {
                        headers.set(HttpHeaders.CONTENT_TYPE, contentType);
                    }
                })
                .body(body == null ? new byte[0] : body)
                .exchange((request, response) -> {
                    HttpHeaders headers = new HttpHeaders();
                    if (response.getHeaders().getContentType() != null) {
                        headers.setContentType(response.getHeaders().getContentType());
                    }
                    byte[] responseBody = response.getBody().readAllBytes();
                    return ResponseEntity.status(response.getStatusCode())
                            .headers(headers)
                            .body(responseBody);
                });
    }
}
