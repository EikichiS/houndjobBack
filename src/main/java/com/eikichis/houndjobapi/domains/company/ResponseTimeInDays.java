package com.eikichis.houndjobapi.domains.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseTimeInDays {
    @JsonProperty("min")
    private int min;

    @JsonProperty("max")
    private int max;
}
