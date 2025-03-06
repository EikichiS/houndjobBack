package com.eikichis.houndjobapi.services.Impl;

import com.eikichis.houndjobapi.domains.JobPosting;
import com.eikichis.houndjobapi.domains.Remotive;
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
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        List<JobDTO> jobPostingDTOs = new ArrayList<>();
        jobPostingDTOs.addAll(getOnBoard(query));
        jobPostingDTOs.addAll(remotive(query));
        return jobPostingDTOs;
    }

    private List<JobDTO> getOnBoard(String query) throws ApiException {
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
            List<JobDTO> jobPostingDTOs = mapToDTOGetOnBoard(jobPostings);

            return jobPostingDTOs;

        } catch (Exception e) {
            throw new ApiException("Error al conectar con la API: " + e.getMessage(), e);
        }
    }

    private List<JobDTO> remotive(String query) throws ApiException {
        try {
            String baseUrl = "https://remotive.com/api/remote-jobs";

            String URL = UriComponentsBuilder
                    .fromHttpUrl(baseUrl)
                    .queryParam("search", query) // Remotive usa "search" en lugar de "category" para búsquedas generales
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            System.out.println("URL generada (Remotive): " + URL);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ApiException("Error en la API de Remotive: " + response.statusCode() + " - " + response.body());
            }

            JsonNode rootNode = mapper.readTree(response.body());
            JsonNode jobsNode = rootNode.get("jobs"); // Remotive usa "jobs", no "data"

            if (jobsNode == null || !jobsNode.isArray()) {
                throw new ApiException("La respuesta de Remotive no contiene datos válidos.");
            }

            List<Remotive> remotiveJobs = new ArrayList<>();
            for (JsonNode node : jobsNode) {
                Remotive remotiveJob = mapper.treeToValue(node, Remotive.class);
                remotiveJobs.add(remotiveJob);
            }

            return mapToDTORemotive(remotiveJobs);

        } catch (Exception e) {
            throw new ApiException("Error al conectar con la API de Remotive: " + e.getMessage(), e);
        }
    }

    // El método mapToDTO permanece igual
    private List<JobDTO> mapToDTOGetOnBoard(List<JobPosting> jobPostings) {
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

    private List<JobDTO> mapToDTORemotive(List<Remotive> remotiveJobs) {
        List<JobDTO> dtos = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(java.time.ZoneOffset.UTC);

        for (Remotive job : remotiveJobs) {
            JobDTO dto = new JobDTO();
            dto.setId(String.valueOf(job.getId()));
            dto.setType(null);
            dto.setAttributes(new Attributes());
            dto.getAttributes().setTitle(job.getTitle());
            dto.getAttributes().setCompany(job.getCompany_name());
            dto.getAttributes().setCountry(job.getCandidate_required_location());
            dto.getAttributes().setPortal("Remotive");

            String publicationDate = job.getPublication_date();
            if (publicationDate != null && !publicationDate.isEmpty()) {
                try {
                    Instant instant = Instant.from(formatter.parse(publicationDate));
                    long timestamp = instant.getEpochSecond();
                    dto.getAttributes().setCreation_date(String.valueOf(timestamp));
                } catch (DateTimeParseException e) {
                    System.err.println("Error al parsear la fecha de Remotive: " + publicationDate + " - " + e.getMessage());
                    dto.getAttributes().setCreation_date(null);
                }
            } else {
                dto.getAttributes().setCreation_date(null);
            }

            dto.setPublicUrl(job.getUrl());
            dtos.add(dto);
        }
        return dtos;
    }
}
