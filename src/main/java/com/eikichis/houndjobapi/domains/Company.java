package com.eikichis.houndjobapi.domains;

import com.eikichis.houndjobapi.domains.company.CompanyData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Company {
    @JsonProperty("data")
    private CompanyData data;
}

