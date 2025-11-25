package com.vanheurck.dpp.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vanheurck.dpp.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {
	Optional<Supplier> findByCode(String code);

    List<Supplier> findByIsActiveTrue();
}