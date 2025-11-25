package com.vanheurck.dpp.dto;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class MaterialResponse {
    private Long id;
    private String code;
    private String name;
    private String category;
    private Boolean isActive;
    private OffsetDateTime createdAtUtc;
    private OffsetDateTime updatedAtUtc;
}
