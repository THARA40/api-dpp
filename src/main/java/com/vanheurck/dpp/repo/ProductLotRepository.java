package com.vanheurck.dpp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vanheurck.dpp.entity.ProductLot;

import java.util.List;
import java.util.Optional;

public interface ProductLotRepository extends JpaRepository<ProductLot, Long> {
    List<ProductLot> findByProductId(Long productId);
    
 // Take the latest lot for the product (you can change logic as needed)
    Optional<ProductLot> findFirstByProductIdOrderByIdDesc(Long productId);
}
