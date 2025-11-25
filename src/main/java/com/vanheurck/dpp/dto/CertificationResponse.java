package com.vanheurck.dpp.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class CertificationResponse {
    private Long id;
    private String certificationType;
    private String certificateNo;
    private String issuerName;
    private OffsetDateTime issueDateUtc;
    private OffsetDateTime expiryDateUtc;
    private Boolean revoked;
}
