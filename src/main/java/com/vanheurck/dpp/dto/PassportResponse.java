package com.vanheurck.dpp.dto;

import java.time.OffsetDateTime;

import com.vanheurck.dpp.util.PassportStatus;

import lombok.Data;

@Data
public class PassportResponse {

    private Long id;
    private Long productId;
    private Integer versionNo;
    private PassportStatus status;
    private String serialNumber;
    private OffsetDateTime issueDateUtc;
    private OffsetDateTime expiryDateUtc;
    private String qrCodeValue;
    private String dataJson;
    private OffsetDateTime createdAtUtc;
    private OffsetDateTime updatedAtUtc;
}