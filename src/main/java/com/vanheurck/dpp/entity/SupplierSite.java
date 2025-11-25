package com.vanheurck.dpp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "supplier_site", schema = "dpp")
public class SupplierSite {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_id")
    private Long id;
    
    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "site_code", nullable = false)
    private String siteCode;

    @Column(name = "name")
    private String name;

    @Column(name = "gln")
    private String gln;

    @Column(name = "address_json")
    private String addressJson;

    @Column(name = "is_active")
    private Boolean isActive;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", insertable = false, updatable = false)
    private Supplier supplier;
}
