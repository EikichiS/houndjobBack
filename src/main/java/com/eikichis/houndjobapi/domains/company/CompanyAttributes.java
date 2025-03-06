package com.eikichis.houndjobapi.domains.company;

import com.eikichis.houndjobapi.domains.ResponseTimeInDays;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompanyAttributes {
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("long_description")
    private String longDescription;

    @JsonProperty("projects")
    private String projects;

    @JsonProperty("benefits")
    private String benefits;

    @JsonProperty("web")
    private String web;

    @JsonProperty("twitter")
    private String twitter;

    @JsonProperty("github")
    private String github;

    @JsonProperty("facebook")
    private String facebook;

    @JsonProperty("angellist")
    private String angellist;

    @JsonProperty("country")
    private String country;

    @JsonProperty("response_time_in_days")
    private ResponseTimeInDays responseTimeInDays;

    @JsonProperty("logo")
    private String logo;
}
