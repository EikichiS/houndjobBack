package com.eikichis.houndjobapi.services;

import com.eikichis.houndjobapi.dto.JobDTO;
import com.eikichis.houndjobapi.dto.Attributes;
import com.eikichis.houndjobapi.exception.ApiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

@Component
public class BneApiClient extends BaseApiClient {

    private static final String BASE_URL = "https://www.bne.cl/data/ofertas/buscarListas";

    public BneApiClient(HttpClient httpClient, ObjectMapper objectMapper) {
        super(httpClient, objectMapper);
    }

    public List<JobDTO> fetchJobs(String query,String empPagina,String page) throws ApiException {
        String url = buildUrl(BASE_URL,
                "mostrar", "empleo",
                "numPaginaRecuperar", page,
                "numResultadosPorPagina", empPagina,
                "clasificarYPaginar", "true",
                "textoLibre", query);
        System.out.println("URL generada (BNE): " + url);

        JsonNode rootNode = fetchJson(url);
        JsonNode paginaOfertasNode = rootNode.get("paginaOfertas");
        if (paginaOfertasNode == null) {
            throw new ApiException("La respuesta de BNE no contiene el campo 'paginaOfertas'.");
        }

        JsonNode resultadosNode = paginaOfertasNode.get("resultados");
        if (resultadosNode == null || !resultadosNode.isArray()) {
            System.out.println("No se encontraron resultados para el query: " + query);
            return new ArrayList<>();
        }

        List<JobDTO> jobDTOs = new ArrayList<>();
        for (JsonNode node : resultadosNode) {
            JobDTO jobDTO = new JobDTO();
            jobDTO.setId(node.get("id").asText());
            jobDTO.setType("job");
            jobDTO.setPublicUrl("https://www.bne.cl/oferta/" + node.get("codigo").asText());
            jobDTO.setDescription(node.get("descripcion").asText());

            Attributes attributes = new Attributes();
            attributes.setTitle(node.get("titulo").asText());
            attributes.setCompany(node.get("empresa").asText());
            attributes.setCountry("Chile");
            attributes.setRegion(node.get("region").asText());
            attributes.setPortal("BNE.cl");
            attributes.setLogo_url("https://www.bne.cl/data/img/logo-oferta?cod=" + node.get("codigo").asText());
            attributes.setCreation_date(node.get("fecha").asText(null));

            jobDTO.setAttributes(attributes);
            jobDTOs.add(jobDTO);
        }

        return jobDTOs;
    }
}
