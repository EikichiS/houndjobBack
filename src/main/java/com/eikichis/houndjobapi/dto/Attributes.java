package com.eikichis.houndjobapi.dto;

import lombok.Data;

@Data
public class Attributes {
    private String title;
    private String company;
    private String country;
    private String region;
    private String minSalary;
    private String maxSalary;
    private String category;
    private String portal;
    private String creation_date;
}
