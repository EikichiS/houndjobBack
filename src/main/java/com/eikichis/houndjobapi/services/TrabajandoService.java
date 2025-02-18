package com.eikichis.houndjobapi.services;

import com.eikichis.houndjobapi.dto.Job;

import java.io.IOException;
import java.util.List;

public interface TrabajandoService {

    public List<Job> scrapeJobs(String searchQuery) throws IOException;
}
