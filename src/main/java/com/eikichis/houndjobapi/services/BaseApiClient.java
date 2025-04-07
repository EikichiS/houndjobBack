package com.eikichis.houndjobapi.services;

import com.eikichis.houndjobapi.exception.ApiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public abstract class BaseApiClient {

    protected final HttpClient httpClient;
    protected final ObjectMapper objectMapper;

    public BaseApiClient(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    protected JsonNode fetchJson(String url) throws ApiException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ApiException("Error en la API: " + response.statusCode() + " - " + response.body());
            }

            return objectMapper.readTree(response.body());
        } catch (Exception e) {
            throw new ApiException("Error al conectar con la API: " + e.getMessage(), e);
        }
    }

    protected String buildUrl(String baseUrl, String... queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
        for (int i = 0; i < queryParams.length; i += 2) {
            builder.queryParam(queryParams[i], queryParams[i + 1]);
        }
        return builder.encode(StandardCharsets.UTF_8).toUriString();
    }
}
