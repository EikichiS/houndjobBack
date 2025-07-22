package com.eikichis.houndjobapi.services.mapper;

import com.eikichis.houndjobapi.domains.JobPosting;
import com.eikichis.houndjobapi.domains.Remotive;
import com.eikichis.houndjobapi.dto.JobDTO;
import com.eikichis.houndjobapi.dto.Attributes;
import com.eikichis.houndjobapi.exception.ApiException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobMapperService {

    private static final DateTimeFormatter REMOTIVE_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(java.time.ZoneOffset.UTC);

    public List<JobDTO> mapToDTOGetOnBoard(List<JobPosting> jobPostings) {
        List<JobDTO> dtos = new ArrayList<>();
        for (JobPosting job : jobPostings) {
            JobDTO dto = new JobDTO();
            dto.setId(job.getId());
            dto.setDescription(job.getAttributes().getDescription());
            dto.setAttributes(new Attributes());
            dto.getAttributes().setTitle(job.getAttributes().getTitle());
            dto.getAttributes().setCompany(job.getAttributes().getCompany().getData().getAttributes().getName());
            dto.getAttributes().setCountry(Optional.ofNullable(job.getAttributes().getCountries())
                    .stream()
                    .flatMap(List::stream)
                    .findFirst()
                    .orElse(null));
            dto.getAttributes().setMinSalary(job.getAttributes().getMin_salary());
            dto.getAttributes().setMaxSalary(job.getAttributes().getMax_salary());
            dto.getAttributes().setCategory(job.getAttributes().getCategory());
            dto.getAttributes().setPortal("Get On Board");
            dto.getAttributes().setCreation_date(job.getAttributes().getCreationDate());
            dto.getAttributes().setLogo_url(job.getAttributes().getCompany().getData().getAttributes().getLogo());
            dto.setPublicUrl(job.getLinks().getPublicUrl());
            dtos.add(dto);
        }
        return dtos;
    }

    public List<JobDTO> mapToDTORemotive(List<Remotive> remotiveJobs) {
        List<JobDTO> dtos = new ArrayList<>();
        for (Remotive job : remotiveJobs) {
            JobDTO dto = new JobDTO();
            dto.setId(String.valueOf(job.getId()));
            dto.setType(null);
            dto.setAttributes(new Attributes());
            dto.getAttributes().setTitle(job.getTitle());
            dto.getAttributes().setCompany(job.getCompany_name());
            dto.getAttributes().setCountry(job.getCandidate_required_location());
            dto.getAttributes().setPortal("Remotive");

            String publicationDate = job.getPublication_date();
            if (publicationDate != null && !publicationDate.isEmpty()) {
                try {
                    Instant instant = Instant.from(REMOTIVE_DATE_FORMATTER.parse(publicationDate));
                    dto.getAttributes().setCreation_date(String.valueOf(instant.getEpochSecond()));
                } catch (DateTimeParseException e) {
                    System.err.println("Error al parsear la fecha de Remotive: " + publicationDate + " - " + e.getMessage());
                    dto.getAttributes().setCreation_date(null);
                }
            } else {
                dto.getAttributes().setCreation_date(null);
            }

            dto.setPublicUrl(job.getUrl());
            dtos.add(dto);
        }
        return dtos;
    }
    public List<JobDTO> mapToDtoTrabajo(JsonNode rootNode) throws ApiException {
        if (!rootNode.isArray()) {
            throw new ApiException("La respuesta de TrabajoConSentido no es un array esperado");
        }

        List<JobDTO> jobDTOs = new ArrayList<>();

        for (JsonNode offerNode : rootNode) {
            JobDTO jobDTO = new JobDTO();
            jobDTO.setId(offerNode.get("id").asText());
            jobDTO.setType("job");

            Attributes attributes = new Attributes();
            attributes.setTitle(offerNode.get("title").asText());
            attributes.setCompany(offerNode.get("organization").get("name").asText());
            attributes.setRegion(offerNode.get("city").asText());
            attributes.setPortal("TrabajoConSentido");
            attributes.setCreation_date(offerNode.get("createdAt").asText());

            // Manejo de salario (ajusta según la estructura real de la API)
            if (offerNode.has("salary")) {
                JsonNode salaryNode = offerNode.get("salary");
                attributes.setMinSalary(salaryNode.get("range").get(0).asText());
                attributes.setMaxSalary(salaryNode.get("range").get(1).asText());
            }
            jobDTO.setAttributes(attributes);
            jobDTOs.add(jobDTO);
        }

        return jobDTOs;
    }
}
