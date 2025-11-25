package com.vanheurck.dpp.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vanheurck.dpp.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findBySku(String sku);

    List<Product> findByIsActiveTrue();
}