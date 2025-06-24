package com.eikichis.houndjobapi.services.client;

import com.eikichis.houndjobapi.dto.JobDTO;
import com.eikichis.houndjobapi.exception.ApiException;
import com.eikichis.houndjobapi.services.mapper.JobMapperService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;
import java.util.List;

@Component
public class TrabajoConSentidoClient extends BaseApiClient {

    private static final String BASE_URL = "https://api.trabajoconsentido.com/offers";

    private final JobMapperService jobMapperService;
    public TrabajoConSentidoClient(HttpClient httpClient, ObjectMapper objectMapper,
                                   JobMapperService jobMapperService) {
        super(httpClient, objectMapper);
        this.jobMapperService = jobMapperService;
    }

    public List<JobDTO> fetchJobs() throws ApiException {
        String url = buildUrl(BASE_URL);
        System.out.println("URL generada (Trabajos con Sentido): " + url);

        JsonNode rootNode = fetchJson(url);
        JsonNode dataNode = rootNode.get("content").get("offers");

        if (dataNode == null || !dataNode.isArray()) {
            throw new ApiException("La respuesta de GetOnBoard no contiene datos válidos.");
        }

        return jobMapperService.mapToDtoTrabajo(dataNode);
    }
}
