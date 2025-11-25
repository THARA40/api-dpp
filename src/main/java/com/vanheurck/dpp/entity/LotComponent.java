package com.vanheurck.dpp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "lot_component", schema = "dpp")
public class LotComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lot_component_id")
    private Long id;

    @Column(name = "parent_product_lot_id")
    private Long parentProductLotId;

    @Column(name = "parent_material_lot_id")
    private Long parentMaterialLotId;

    @Column(name = "component_product_lot_id")
    private Long componentProductLotId;

    @Column(name = "component_material_lot_id")
    private Long componentMaterialLotId;

    @Column(name = "qty")
    private Double qty;

    @Column(name = "uom")
    private String uom;

    // Parent can be product lot OR material lot (exactly one not null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_product_lot_id", insertable = false, updatable = false)
    private ProductLot parentProductLot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_material_lot_id", insertable = false, updatable = false)
    private MaterialLot parentMaterialLot;

    // Component can be product lot OR material lot (exactly one not null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_product_lot_id", insertable = false, updatable = false)
    private ProductLot componentProductLot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_material_lot_id", insertable = false, updatable = false)
    private MaterialLot componentMaterialLot;
}
