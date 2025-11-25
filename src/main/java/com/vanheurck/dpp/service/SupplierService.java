package com.vanheurck.dpp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.vanheurck.dpp.dto.CertificationResponse;
import com.vanheurck.dpp.dto.SupplierRequest;
import com.vanheurck.dpp.dto.SupplierResponse;
import com.vanheurck.dpp.dto.SupplierSiteResponse;
import com.vanheurck.dpp.entity.Certification;
import com.vanheurck.dpp.entity.Supplier;
import com.vanheurck.dpp.entity.SupplierSite;
import com.vanheurck.dpp.filter.SupplierFilter;
import com.vanheurck.dpp.repo.CertificationRepository;
import com.vanheurck.dpp.repo.SupplierRepository;
import com.vanheurck.dpp.repo.SupplierSiteRepository;
import com.vanheurck.dpp.specification.SupplierSpecifications;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepo;
    private final SupplierSiteRepository siteRepo;
    private final CertificationRepository certRepo;

    public SupplierService(SupplierRepository supplierRepo,
    		SupplierSiteRepository siteRepo,
            CertificationRepository certRepo) {
        this.supplierRepo = supplierRepo;
        this.siteRepo = siteRepo;
        this.certRepo = certRepo;
    }

    public SupplierResponse create(SupplierRequest req) {
    	Supplier entity = new Supplier();
        apply(req, entity);
        Supplier saved = supplierRepo.save(entity);
        return toResponse(saved);
    }

    public SupplierResponse update(Long id, SupplierRequest req) {
    	Supplier entity = supplierRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Supplier not found: " + id));
        apply(req, entity);
        Supplier saved = supplierRepo.save(entity);
        return toResponse(saved);
    }

    public SupplierResponse getById(Long id) {
    	Supplier entity = supplierRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Supplier not found: " + id));
        return toResponse(entity);
    }

    public SupplierResponse getByCode(String code) {
    	Supplier entity = supplierRepo.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Supplier not found for code: " + code));
        return toResponse(entity);
    }

    public List<SupplierResponse> getAllActive() {
        return supplierRepo.findByIsActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void deactivate(Long id) {
    	Supplier entity = supplierRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Supplier not found: " + id));
        entity.setIsActive(false);
        supplierRepo.save(entity);
    }
    
    public List<SupplierSiteResponse> getSites(Long supplierId) {
        // ensure supplier exists
        supplierRepo.findById(supplierId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Supplier not found: " + supplierId));

        return siteRepo.findBySupplierId(supplierId).stream()
                .map(this::toSiteResponse)
                .collect(Collectors.toList());
    }
    
    public List<CertificationResponse> getCertifications(Long supplierId) {
        // ensure supplier exists
        supplierRepo.findById(supplierId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Supplier not found: " + supplierId));

        return certRepo.findBySupplierId(supplierId).stream()
                .map(this::toCertResponse)
                .collect(Collectors.toList());
    }
    
    public Page<SupplierResponse> searchSuppliers(String countryCode,
            Boolean active,
            Pageable pageable) {

		SupplierFilter filter = SupplierFilter.builder()
		.countryCode(countryCode)
		.active(active)
		.build();
		
		var spec = SupplierSpecifications.build(filter);
		
		return supplierRepo.findAll(spec, pageable)
		.map(this::toResponse);
	}

    private void apply(SupplierRequest req, Supplier entity) {
        entity.setCode(req.getCode());
        entity.setName(req.getName());
        entity.setIsActive(req.getIsActive());
    }

    private SupplierResponse toResponse(Supplier entity) {
    	SupplierResponse dto = new SupplierResponse();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAtUtc(entity.getCreatedAtUtc());
        dto.setUpdatedAtUtc(entity.getUpdatedAtUtc());
        return dto;
    }
    
    private SupplierSiteResponse toSiteResponse(SupplierSite site) {
        SupplierSiteResponse dto = new SupplierSiteResponse();
        dto.setId(site.getId());
        dto.setSiteCode(site.getSiteCode());
        dto.setName(site.getName());
        dto.setGln(site.getGln());
        return dto;
    }

    private CertificationResponse toCertResponse(Certification c) {
        CertificationResponse dto = new CertificationResponse();
        dto.setId(c.getId());
        dto.setCertificationType(c.getCertificationType());
        dto.setCertificateNo(c.getCertificateNo());
        dto.setIssuerName(c.getIssuerName());
        dto.setIssueDateUtc(c.getIssueDateUtc());
        dto.setExpiryDateUtc(c.getExpiryDateUtc());
        dto.setRevoked(Boolean.TRUE.equals(c.getIsRevoked()));
        return dto;
    }
}
