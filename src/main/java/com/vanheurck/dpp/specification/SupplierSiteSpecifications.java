package com.vanheurck.dpp.specification;

import org.springframework.data.jpa.domain.Specification;

import com.vanheurck.dpp.entity.SupplierSite;
import com.vanheurck.dpp.filter.SupplierSiteFilter;

public class SupplierSiteSpecifications {

    public static Specification<SupplierSite> build(SupplierSiteFilter filter) {
        Specification<SupplierSite> spec = Specification.where(null);

        if (filter.getCountryCode() != null && !filter.getCountryCode().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("countryCode"), filter.getCountryCode()));
        }

        if (filter.getActive() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("active"), filter.getActive()));
        }
        
        if (filter.getSupplierId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("supplierId"), filter.getSupplierId()));
        }

        return spec;
    }
}
