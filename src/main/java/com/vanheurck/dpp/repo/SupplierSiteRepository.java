package com.vanheurck.dpp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vanheurck.dpp.entity.SupplierSite;

import java.util.List;

public interface SupplierSiteRepository extends JpaRepository<SupplierSite, Long> {

    // supplier → sites
    List<SupplierSite> findBySupplierId(Long supplierId);
}
