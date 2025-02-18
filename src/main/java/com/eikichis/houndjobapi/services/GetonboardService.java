package com.eikichis.houndjobapi.services;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface GetonboardService {

    public List<JsonNode> getOferts() throws Exception;
}
