package com.vanheurck.dpp.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ProductResponse {

    private Long id;
    private String sku;
    private String name;
    private String category;
    private String unitOfMeasure;
    private Boolean isActive;
    private OffsetDateTime createdAtUtc;
    private OffsetDateTime updatedAtUtc;
}
