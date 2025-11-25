package com.vanheurck.dpp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetDateTime;

import com.vanheurck.dpp.util.PassportStatus;

@Data
public class PassportRequest {

    @NotNull
    private Long productId;

    @NotNull
    private Integer versionNo;

    @NotNull
    private PassportStatus status;

    @Size(max = 100)
    private String serialNumber;

    private OffsetDateTime issueDateUtc;
    private OffsetDateTime expiryDateUtc;

    @Size(max = 300)
    private String qrCodeValue;

    // JSON as string – you can validate at service level if needed
    private String dataJson;
}