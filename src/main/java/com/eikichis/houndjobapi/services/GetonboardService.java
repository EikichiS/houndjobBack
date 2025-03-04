package com.eikichis.houndjobapi.services;

import com.eikichis.houndjobapi.domains.JobPosting;
import com.eikichis.houndjobapi.dto.JobDTO;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface GetonboardService {

    public List<JobDTO> getOferts(String query) throws Exception;
}
