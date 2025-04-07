package com.eikichis.houndjobapi.services;

import com.eikichis.houndjobapi.domains.JobPosting;
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
public class GetOnBoardApiClient extends BaseApiClient {

    private static final String BASE_URL = "https://www.getonbrd.com/api/v0/search/jobs";
    private final JobMapperService jobMapperService;

    public GetOnBoardApiClient(HttpClient httpClient, ObjectMapper objectMapper, JobMapperService jobMapperService) {
        super(httpClient, objectMapper);
        this.jobMapperService = jobMapperService;
    }

    public List<JobDTO> fetchJobs(String query,String empPagina,String page) throws ApiException {
        String url = buildUrl(BASE_URL, "query", query,"per_page", empPagina,
                "page", page, "expand", "[\"company\"]");
        System.out.println("URL generada (GetOnBoard): " + url);

        JsonNode rootNode = fetchJson(url);
        JsonNode dataNode = rootNode.get("data");

        if (dataNode == null || !dataNode.isArray()) {
            throw new ApiException("La respuesta de GetOnBoard no contiene datos válidos.");
        }

        List<JobPosting> jobPostings = new ArrayList<>();
        for (JsonNode node : dataNode) {
            JobPosting jobPosting = null;
            try {
                jobPosting = objectMapper.treeToValue(node, JobPosting.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            jobPostings.add(jobPosting);
        }

        return jobMapperService.mapToDTOGetOnBoard(jobPostings);
    }
}
