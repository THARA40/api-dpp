package com.vanheurck.dpp.dto;

import lombok.Data;

@Data
public class SupplierSiteResponse {
    private Long id;
    private String siteCode;
    private String name;
    private String gln;
}
