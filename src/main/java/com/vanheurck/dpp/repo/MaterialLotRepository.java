package com.vanheurck.dpp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vanheurck.dpp.entity.MaterialLot;

import java.util.List;

public interface MaterialLotRepository extends JpaRepository<MaterialLot, Long> {
    List<MaterialLot> findByIdIn(Iterable<Long> ids);
    
    List<MaterialLot> findByMaterial_Id(Long materialId);
}
