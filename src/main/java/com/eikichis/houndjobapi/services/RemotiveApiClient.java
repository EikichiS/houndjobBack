package com.eikichis.houndjobapi.services;

import com.eikichis.houndjobapi.domains.Remotive;
import com.eikichis.houndjobapi.dto.JobDTO;
import com.eikichis.houndjobapi.exception.ApiException;
import com.eikichis.houndjobapi.services.BaseApiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

@Component
public class RemotiveApiClient extends BaseApiClient {

    private static final String BASE_URL = "https://remotive.com/api/remote-jobs";
    private final JobMapperService jobMapperService;

    public RemotiveApiClient(HttpClient httpClient, ObjectMapper objectMapper, JobMapperService jobMapperService) {
        super(httpClient, objectMapper);
        this.jobMapperService = jobMapperService;
    }

    public List<JobDTO> fetchJobs(String query) throws ApiException {
        String url = buildUrl(BASE_URL, "search", query);
        System.out.println("URL generada (Remotive): " + url);

        JsonNode rootNode = fetchJson(url);
        JsonNode jobsNode = rootNode.get("jobs");

        if (jobsNode == null || !jobsNode.isArray()) {
            throw new ApiException("La respuesta de Remotive no contiene datos válidos.");
        }

        List<Remotive> remotiveJobs = new ArrayList<>();
        for (JsonNode node : jobsNode) {
            Remotive remotiveJob = null;
            try {
                remotiveJob = objectMapper.treeToValue(node, Remotive.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            remotiveJobs.add(remotiveJob);
        }

        return jobMapperService.mapToDTORemotive(remotiveJobs);
    }
}
