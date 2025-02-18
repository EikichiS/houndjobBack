package com.eikichis.houndjobapi.services.Impl;

import com.eikichis.houndjobapi.services.GetonboardService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class GetonboardServiceImpl implements GetonboardService {

    private final RestTemplate restTemplate;

    public GetonboardServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<JsonNode> getOferts() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(restTemplate.getForObject("https://www.getonbrd.com/api/v0/search/jobs?query=Data+Analyst&per_page=10&page=1&expand=[\"company\"]&country_code=CL", String.class));
        // Procesa el JsonNode según tus necesidades
        return Collections.singletonList(rootNode.get("data"));
    }
}
