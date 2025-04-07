package com.eikichis.houndjobapi.services.Impl;

import com.eikichis.houndjobapi.dto.JobDTO;
import com.eikichis.houndjobapi.exception.ApiException;
import com.eikichis.houndjobapi.services.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PublicAPIServiceImpl implements PublicAPIService {

    private final GetOnBoardApiClient getOnBoardClient;
    private final RemotiveApiClient remotiveClient;
    private final BneApiClient bneClient;
    private final JobMapperService jobMapperService;

    public PublicAPIServiceImpl(GetOnBoardApiClient getOnBoardClient,
                                RemotiveApiClient remotiveClient,
                                BneApiClient bneClient,
                                JobMapperService jobMapperService) {
        this.getOnBoardClient = getOnBoardClient;
        this.remotiveClient = remotiveClient;
        this.bneClient = bneClient;
        this.jobMapperService = jobMapperService;
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
        jobDTOs.addAll(getOnBoardClient.fetchJobs("all","100","4"));
        jobDTOs.addAll(bneClient.fetchJobs("","80","4"));
        return jobDTOs;    }
}
