package com.eikichis.houndjobapi.domains.company;

import com.eikichis.houndjobapi.domains.company.CompanyAttributes;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompanyData {
    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("attributes")
    private CompanyAttributes attributes;
}
