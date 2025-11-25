package com.vanheurck.dpp.specification;

import org.springframework.data.jpa.domain.Specification;

import com.vanheurck.dpp.entity.Supplier;
import com.vanheurck.dpp.filter.SupplierFilter;

public class SupplierSpecifications {

    public static Specification<Supplier> build(SupplierFilter filter) {
        Specification<Supplier> spec = Specification.where(null);

        if (filter.getCountryCode() != null && !filter.getCountryCode().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("countryCode"), filter.getCountryCode()));
        }

        if (filter.getActive() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("active"), filter.getActive()));
        }

        return spec;
    }
}
