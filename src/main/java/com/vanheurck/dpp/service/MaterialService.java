package com.vanheurck.dpp.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.vanheurck.dpp.dto.MaterialLotResponse;
import com.vanheurck.dpp.dto.MaterialRequest;
import com.vanheurck.dpp.dto.MaterialResponse;
import com.vanheurck.dpp.entity.Material;
import com.vanheurck.dpp.entity.MaterialLot;
import com.vanheurck.dpp.repo.MaterialLotRepository;
import com.vanheurck.dpp.repo.MaterialRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MaterialService {

    private final MaterialRepository materialRepo;
    private final MaterialLotRepository lotRepo;

    public MaterialService(MaterialRepository repository, MaterialLotRepository lotRepo) {
        this.materialRepo = repository;
        this.lotRepo = lotRepo;
    }

    public MaterialResponse create(MaterialRequest req) {
    	Material entity = new Material();
        apply(req, entity);
        Material saved = materialRepo.save(entity);
        return toResponse(saved);
    }

    public MaterialResponse update(Long id, MaterialRequest req) {
    	Material entity = materialRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Material not found: " + id));
        apply(req, entity);
        Material saved = materialRepo.save(entity);
        return toResponse(saved);
    }

    public MaterialResponse getById(Long id) {
    	Material entity = materialRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Material not found: " + id));
        return toResponse(entity);
    }

    public MaterialResponse getByCode(String code) {
    	Material entity = materialRepo.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Material not found for code: " + code));
        return toResponse(entity);
    }

    public List<MaterialResponse> getAllActive() {
        return materialRepo.findByIsActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void deactivate(Long id) {
    	Material entity = materialRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Material not found: " + id));
        entity.setIsActive(false);
        materialRepo.save(entity);
    }
    
    public List<MaterialLotResponse> getLotsForMaterial(Long materialId) {
        // ensure material exists
        materialRepo.findById(materialId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Material not found: " + materialId));

        return lotRepo.findByMaterial_Id(materialId).stream()
                .map(this::toMaterialLotResponse)
                .collect(Collectors.toList());
    }

    private void apply(MaterialRequest req, Material entity) {
        entity.setCode(req.getCode());
        entity.setName(req.getName());
        entity.setCategory(req.getCategory());
        entity.setIsActive(req.getIsActive());
    }

    private MaterialResponse toResponse(Material entity) {
    	MaterialResponse dto = new MaterialResponse();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setCategory(entity.getCategory());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAtUtc(entity.getCreatedAtUtc());
        dto.setUpdatedAtUtc(entity.getUpdatedAtUtc());
        return dto;
    }
    
    private MaterialLotResponse toMaterialLotResponse(MaterialLot lot) {
        MaterialLotResponse dto = new MaterialLotResponse();
        dto.setId(lot.getId());
        if (lot.getMaterial() != null) {
            dto.setMaterialId(lot.getMaterial().getId());
            dto.setMaterialCode(lot.getMaterial().getCode());
            dto.setMaterialName(lot.getMaterial().getName());
        }
        dto.setLotNo(lot.getLotNo());
        dto.setOriginCountry(lot.getOriginCountry());
        dto.setProdDateUtc(lot.getProdDateUtc());
        dto.setExpiryDateUtc(lot.getExpiryDateUtc());
        dto.setQtyOnHand(lot.getQtyOnHand());
        dto.setUom(lot.getUom());
        dto.setCertStatus(lot.getCertStatus());
        return dto;
    }
}
