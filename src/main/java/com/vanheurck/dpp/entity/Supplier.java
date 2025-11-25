package com.vanheurck.dpp.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "supplier", schema = "dpp")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long id;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "name")
    private String name;

    @Column(name = "supplier_code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "country_code", length = 2)
    private String countryCode;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at_utc", updatable = false)
    private OffsetDateTime createdAtUtc;

    @Column(name = "updated_at_utc")
    private OffsetDateTime updatedAtUtc;

    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        if (createdAtUtc == null) createdAtUtc = now;
        if (updatedAtUtc == null) updatedAtUtc = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAtUtc = OffsetDateTime.now();
    }
}
