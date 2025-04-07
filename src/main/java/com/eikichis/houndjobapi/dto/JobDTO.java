package com.eikichis.houndjobapi.dto;

import lombok.Data;

@Data
public class JobDTO {
    private String id;
    private String type;
    private String description;
    private Attributes attributes;
    private String publicUrl;
}
