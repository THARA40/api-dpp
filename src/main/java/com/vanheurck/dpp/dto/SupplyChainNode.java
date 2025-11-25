package com.vanheurck.dpp.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SupplyChainNode {

    private String nodeType;      // "PRODUCT_LOT" or "MATERIAL_LOT"

    private Long productLotId;
    private Long materialLotId;

    private String lotType;          // "PRODUCT" or "MATERIAL"
    private Long lotId;
    private String lotNo;

    private Long productId;
    private String productSku;
    private String productName;   // if you join to product table

    private Long materialId;
    private String materialCode;  // if you join to material table
    private String materialName;

    private Long supplierId;
    private String supplierName;

    private Long companyId;
    private String companyName;

    private Long siteId;
    private String siteName;

    private Double qty;
    private String uom;

    private List<SupplyChainNode> children = new ArrayList<>();
    
 // Recursively nested components
    private List<SupplyChainNode> components = new ArrayList<>();
}
