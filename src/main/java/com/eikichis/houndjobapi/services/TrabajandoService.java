package com.eikichis.houndjobapi.services;

import com.eikichis.houndjobapi.dto.Job;
import com.eikichis.houndjobapi.dto.Portal;

import java.io.IOException;
import java.util.List;

public interface TrabajandoService {

    public List<Job> scrapeJobs(Portal portal) throws IOException;
}
