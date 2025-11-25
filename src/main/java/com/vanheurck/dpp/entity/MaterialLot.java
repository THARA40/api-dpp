package com.vanheurck.dpp.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

@Entity
@Data
@Table(name = "material_lot", schema = "dpp")
public class MaterialLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_lot_id")
    private Long id;

    @Column(name = "material_id")
    private Long materialId;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "site_id")
    private Long siteId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "lot_no")
    private String lotNo;

    @Column(name = "prod_date_utc")
    private OffsetDateTime prodDateUtc;

    @Column(name = "origin_country")
    private String originCountry;

    @Column(name = "expiry_date_utc")
    private OffsetDateTime expiryDateUtc;

    @Column(name = "qty_on_hand")
    private Double qtyOnHand;

    @Column(name = "uom")
    private String uom;

    @Column(name = "cert_status")
    private String certStatus;

    @Column(name = "attrs_json")
    private String attrsJson;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", insertable = false, updatable = false)
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", insertable = false, updatable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private Company company;
}