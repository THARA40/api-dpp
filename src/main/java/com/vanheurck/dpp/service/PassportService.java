package com.vanheurck.dpp.service;

import com.vanheurck.dpp.dto.PassportRequest;
import com.vanheurck.dpp.dto.PassportResponse;
import com.vanheurck.dpp.entity.Passport;
import com.vanheurck.dpp.repo.PassportRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PassportService {

    private final PassportRepository repository;

    public PassportService(PassportRepository repository) {
        this.repository = repository;
    }

    public PassportResponse create(PassportRequest req) {
        Passport entity = new Passport();
        applyRequestToEntity(req, entity);
        Passport saved = repository.save(entity);
        return toResponse(saved);
    }

    public PassportResponse update(Long id, PassportRequest req) {
        Passport entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Passport not found: " + id));
        applyRequestToEntity(req, entity);
        Passport saved = repository.save(entity);
        return toResponse(saved);
    }

    public PassportResponse getById(Long id) {
        Passport entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Passport not found: " + id));
        return toResponse(entity);
    }

    public PassportResponse getByProductAndVersion(Long productId, Integer versionNo) {
        Passport entity = repository.findByProductIdAndVersionNo(productId, versionNo)
                .orElseThrow(() -> new RuntimeException(
                        "Passport not found for productId=" + productId + " versionNo=" + versionNo));
        return toResponse(entity);
    }

    public List<PassportResponse> getByProduct(Long productId) {
        return repository.findByProductId(productId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private void applyRequestToEntity(PassportRequest req, Passport entity) {
        entity.setProductId(req.getProductId());
        entity.setVersionNo(req.getVersionNo());
        entity.setStatus(req.getStatus());
        entity.setSerialNumber(req.getSerialNumber());
        entity.setIssueDateUtc(req.getIssueDateUtc());
        entity.setExpiryDateUtc(req.getExpiryDateUtc());
        entity.setQrCodeValue(req.getQrCodeValue());
        entity.setDataJson(req.getDataJson());
    }

    private PassportResponse toResponse(Passport entity) {
        PassportResponse dto = new PassportResponse();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProductId());
        dto.setVersionNo(entity.getVersionNo());
        dto.setStatus(entity.getStatus());
        dto.setSerialNumber(entity.getSerialNumber());
        dto.setIssueDateUtc(entity.getIssueDateUtc());
        dto.setExpiryDateUtc(entity.getExpiryDateUtc());
        dto.setQrCodeValue(entity.getQrCodeValue());
        dto.setDataJson(entity.getDataJson());
        dto.setCreatedAtUtc(entity.getCreatedAtUtc());
        dto.setUpdatedAtUtc(entity.getUpdatedAtUtc());
        return dto;
    }
}
