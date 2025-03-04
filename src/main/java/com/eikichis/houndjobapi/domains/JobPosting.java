package com.eikichis.houndjobapi.domains;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPosting {
    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;

    @JsonProperty("attributes")
    private Attributes attributes;

    @JsonProperty("links")
    private Links links;
}
