package com.vanheurck.dpp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vanheurck.dpp.entity.SupplierSite;

import java.util.List;
import java.util.Optional;

public interface SupplierSiteRepository extends JpaRepository<SupplierSite, Long>, JpaSpecificationExecutor<SupplierSite> {

    // supplier → sites
    List<SupplierSite> findBySupplierId(Long supplierId);
    
    List<SupplierSite> findByIsActiveTrue();
    
    Optional<SupplierSite> findByCode(String code);
}
