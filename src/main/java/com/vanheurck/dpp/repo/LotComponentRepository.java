package com.vanheurck.dpp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vanheurck.dpp.entity.LotComponent;
import com.vanheurck.dpp.entity.MaterialLot;
import com.vanheurck.dpp.entity.ProductLot;

import java.util.List;

public interface LotComponentRepository extends JpaRepository<LotComponent, Long> {
    List<LotComponent> findByParentProductLotIdIn(Iterable<Long> parentIds);
    
    List<LotComponent> findByParentProductLot(ProductLot parentProductLot);

    List<LotComponent> findByParentMaterialLot(MaterialLot parentMaterialLot);
    
    List<LotComponent> findByParentProductLotId(Long parentProductLotId);

    List<LotComponent> findByParentMaterialLotId(Long parentMaterialLotId);
}
