package com.vanheurck.dpp.filter;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SupplierFilter {
    String countryCode;
    Boolean active;
}