package com.eikichis.houndjobapi.dto;

import com.eikichis.houndjobapi.dto.getonboard.Attributes;
import lombok.Data;

@Data
public class JobDTO {
    private String id;
    private String type;
    private Attributes attributes;
    private String publicUrl;
}
