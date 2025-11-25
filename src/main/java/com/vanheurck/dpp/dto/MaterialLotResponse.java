package com.vanheurck.dpp.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class MaterialLotResponse {
    private Long id;
    private Long materialId;
    private String materialCode;
    private String materialName;
    private String lotNo;
    private String originCountry;
    private OffsetDateTime prodDateUtc;
    private OffsetDateTime expiryDateUtc;
    private Double qtyOnHand;
    private String uom;
    private String certStatus;
}
