package com.vanheurck.dpp.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductChainResponse {

    private ProductSummary product;
    private List<PassportSummary> passports;
    private List<ProductLotSummary> productLots;
    private List<ComponentNode> components;

    @Data
    public static class ProductSummary {
        private Long id;
        private String sku;
        private String name;
    }

    @Data
    public static class PassportSummary {
        private Long id;
        private Integer versionNo;
        private String status;
        private String serialNumber;
    }

    @Data
    public static class ProductLotSummary {
        private Long id;
        private String lotNo;
    }

    @Data
    public static class ComponentNode {
        private Long parentProductLotId;
        private String componentType;          // "PRODUCT" or "MATERIAL"
        private Long componentLotId;
        private String materialCode;
        private String materialName;
        private String supplierName;
        private String companyName;
        private Double qty;
        private String uom;
    }
}
