package com.eikichis.houndjobapi.services.Impl;

import com.eikichis.houndjobapi.dto.JobDTO;
import com.eikichis.houndjobapi.exception.ApiException;
import com.eikichis.houndjobapi.services.*;
import com.eikichis.houndjobapi.services.client.BneApiClient;
import com.eikichis.houndjobapi.services.client.GetOnBoardApiClient;
import com.eikichis.houndjobapi.services.client.RemotiveApiClient;
import com.eikichis.houndjobapi.services.client.TrabajoConSentidoClient;
import com.eikichis.houndjobapi.services.mapper.JobMapperService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PublicAPIServiceImpl implements PublicAPIService {

    private final GetOnBoardApiClient getOnBoardClient;
    private final RemotiveApiClient remotiveClient;
    private final BneApiClient bneClient;
    private final JobMapperService jobMapperService;

    private final TrabajoConSentidoClient trabajoConSentidoClient;

    public PublicAPIServiceImpl(GetOnBoardApiClient getOnBoardClient,
                                RemotiveApiClient remotiveClient,
                                BneApiClient bneClient,
                                JobMapperService jobMapperService,
                                TrabajoConSentidoClient trabajoConSentidoClient) {
        this.getOnBoardClient = getOnBoardClient;
        this.remotiveClient = remotiveClient;
        this.bneClient = bneClient;
        this.jobMapperService = jobMapperService;
        this.trabajoConSentidoClient = trabajoConSentidoClient;
    }

    @Override
    public List<JobDTO> getOferts(String query) throws ApiException {
        List<JobDTO> jobDTOs = new ArrayList<>();
        jobDTOs.addAll(getOnBoardClient.fetchJobs(query,"120","2"));
        jobDTOs.addAll(bneClient.fetchJobs(query,"40","2"));
        return jobDTOs;
    }

    @Override
    public List<JobDTO> getAllOferts() throws ApiException {
        List<JobDTO> jobDTOs = new ArrayList<>();
        jobDTOs.addAll(getOnBoardClient.fetchJobs("1","100","4"));
        jobDTOs.addAll(bneClient.fetchJobs("","80","4"));
        jobDTOs.addAll(trabajoConSentidoClient.fetchJobs());
        return jobDTOs;
    }
}
