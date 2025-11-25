package com.vanheurck.dpp.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product", schema = "dpp")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "sku")
    private String sku;

    @Column(name = "name")
    private String name;
    
    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure;

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
