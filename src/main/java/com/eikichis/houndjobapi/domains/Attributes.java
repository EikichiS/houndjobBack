package com.eikichis.houndjobapi.domains;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attributes {
    @JsonProperty("title")
    private String title;

    @JsonProperty("description_headline")
    private String descriptionHeadline;

    @JsonProperty("description")
    private String description;

    @JsonProperty("company")
    private Company company;

    @JsonProperty("countries")
    private List<String> countries;

    @JsonProperty("portal")
    private String portal;

    @JsonProperty("published_at")
    private String creationDate;

    @JsonProperty("min_salary")
    private String min_salary;

    @JsonProperty("max_salary")
    private String max_salary;

    @JsonProperty("categories")
    private String category;

}
