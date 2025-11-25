package com.vanheurck.dpp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Table(name = "certification", schema = "dpp")
@Data
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_id")
    private Long id;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "certification_type", nullable = false)
    private String certificationType;

    @Column(name = "certificate_no", nullable = false)
    private String certificateNo;

    @Column(name = "issuer_name", nullable = false)
    private String issuerName;

    @Column(name = "issue_date_utc", nullable = false)
    private OffsetDateTime issueDateUtc;

    @Column(name = "expiry_date_utc")
    private OffsetDateTime expiryDateUtc;

    @Column(name = "scope_desc")
    private String scopeDesc;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "is_revoked")
    private Boolean isRevoked;
}