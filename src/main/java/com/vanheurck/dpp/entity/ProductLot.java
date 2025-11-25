package com.vanheurck.dpp.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

@Entity
@Data
@Table(name = "product_lot", schema = "dpp")
public class ProductLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_lot_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "lot_no")
    private String lotNo;

    @Column(name = "prod_date_utc")
    private OffsetDateTime prodDateUtc;

    @Column(name = "site_id")
    private Long siteId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "expiry_date_utc")
    private OffsetDateTime expiryDateUtc;

    @Column(name = "qty")
    private Double qty;

    @Column(name = "uom")
    private String uom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", insertable = false, updatable = false)
    private SupplierSite site;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private Company company;
}
