package com.vanheurck.dpp.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "material", schema = "dpp")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;
    
    @Column(name = "category", length = 100)
    private String category;
    
    @Column(name = "spec_json")
    private String specJson;

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
