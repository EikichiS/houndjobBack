package com.eikichis.houndjobapi.services.Impl;

import com.eikichis.houndjobapi.domains.JobPosting;
import com.eikichis.houndjobapi.dto.JobDTO;
import com.eikichis.houndjobapi.dto.getonboard.Attributes;
import com.eikichis.houndjobapi.exception.ApiException;
import com.eikichis.houndjobapi.services.GetonboardService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetonboardServiceImpl implements GetonboardService {

    private final RestTemplate restTemplate;

    public GetonboardServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<JobDTO> getOferts(String query) throws ApiException {
        try {
            String baseUrl = "https://www.getonbrd.com/api/v0/search/jobs";
            String expand = "[\"company\"]"; // Lo pasamos como string literal

            // Construir la URL sin codificar los corchetes de expand manualmente
            String URL = UriComponentsBuilder
                    .fromHttpUrl(baseUrl)
                    .queryParam("query", query)
                    .queryParam("expand", expand) // Pasamos el string tal cual
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            System.out.println("URL generada: " + URL); // Para depuración

            // Configurar ObjectMapper
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Configurar HttpClient
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            // Realizar la solicitud a la API
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar el código de estado HTTP
            if (response.statusCode() != 200) {
                throw new ApiException("Error en la API: " + response.statusCode() + " - " + response.body());
            }

            // Procesar la respuesta JSON
            JsonNode rootNode = mapper.readTree(response.body());
            JsonNode dataNode = rootNode.get("data");

            if (dataNode == null || !dataNode.isArray()) {
                throw new ApiException("La respuesta de la API no contiene datos válidos.");
            }

            // Deserializar cada elemento en la lista de JobPosting
            List<JobPosting> jobPostings = new ArrayList<>();
            for (JsonNode node : dataNode) {
                JobPosting jobPosting = mapper.treeToValue(node, JobPosting.class);
                jobPostings.add(jobPosting);
            }

            // Mapear JobPosting a JobPostingDTO
            List<JobDTO> jobPostingDTOs = mapToDTO(jobPostings);

            return jobPostingDTOs;

        } catch (Exception e) {
            throw new ApiException("Error al conectar con la API: " + e.getMessage(), e);
        }
    }

    // El método mapToDTO permanece igual
    private List<JobDTO> mapToDTO(List<JobPosting> jobPostings) {
        List<JobDTO> dtos = new ArrayList<>();
        for (JobPosting job : jobPostings) {
            JobDTO dto = new JobDTO();
            dto.setId(job.getId());
            dto.setAttributes(new Attributes());
            dto.getAttributes().setTitle(job.getAttributes().getTitle());
            dto.getAttributes().setCompany(job.getAttributes().getCompany().getData().getAttributes().getName());
            dto.getAttributes().setCountry(job.getAttributes().getCountries().get(0));
            dto.getAttributes().setPortal("Get On Board");
            dto.getAttributes().setCreation_date(job.getAttributes().getCreationDate());
            dto.setPublicUrl(job.getLinks().getPublicUrl());
            dtos.add(dto);
        }
        return dtos;
    }
}
