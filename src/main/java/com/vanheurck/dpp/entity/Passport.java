package com.vanheurck.dpp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

import com.vanheurck.dpp.util.PassportStatus;

@Entity
@Data
@Table(name = "passport", schema = "dpp")
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passport_id")
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "version_no", nullable = false)
    private Integer versionNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PassportStatus status;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "issue_date_utc")
    private OffsetDateTime issueDateUtc;

    @Column(name = "expiry_date_utc")
    private OffsetDateTime expiryDateUtc;

    @Column(name = "qr_code_value", length = 300)
    private String qrCodeValue;

    @Column(name = "data_json")
    private String dataJson;

    @Column(name = "created_at_utc", updatable = false)
    private OffsetDateTime createdAtUtc;

    @Column(name = "updated_at_utc")
    private OffsetDateTime updatedAtUtc;

    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        if (createdAtUtc == null) {
            createdAtUtc = now;
        }
        if (updatedAtUtc == null) {
            updatedAtUtc = now;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAtUtc = OffsetDateTime.now();
    }
}
