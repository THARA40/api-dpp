package com.vanheurck.dpp.filter;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SupplierSiteFilter {
    String countryCode;
    Boolean active;
    Long supplierId;
}