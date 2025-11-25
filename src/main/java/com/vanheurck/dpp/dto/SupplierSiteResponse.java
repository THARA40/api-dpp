package com.vanheurck.dpp.dto;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class SupplierSiteResponse {
    private Long id;
    private String siteCode;
    private String name;
    private String gln;
    private Boolean isActive;
    private OffsetDateTime createdAtUtc;
    private OffsetDateTime updatedAtUtc;
}
