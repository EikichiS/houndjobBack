package com.eikichis.houndjobapi.services;

import com.eikichis.houndjobapi.dto.JobDTO;
import com.eikichis.houndjobapi.exception.ApiException;

import java.util.List;

public interface PublicAPIService {

    List<JobDTO> getOferts(String query) throws Exception;

    List<JobDTO> getAllOferts() throws ApiException;

}
