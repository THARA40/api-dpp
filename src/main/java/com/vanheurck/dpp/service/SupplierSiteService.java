package com.vanheurck.dpp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.vanheurck.dpp.dto.SupplierSiteRequest;
import com.vanheurck.dpp.dto.SupplierSiteResponse;
import com.vanheurck.dpp.entity.SupplierSite;
import com.vanheurck.dpp.filter.SupplierSiteFilter;
import com.vanheurck.dpp.repo.SupplierSiteRepository;
import com.vanheurck.dpp.specification.SupplierSiteSpecifications;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierSiteService {

    private final SupplierSiteRepository siteRepo;

    public SupplierSiteService(SupplierSiteRepository siteRepo) {
        this.siteRepo = siteRepo;
    }

    public SupplierSiteResponse create(SupplierSiteRequest req) {
    	SupplierSite entity = new SupplierSite();
        apply(req, entity);
        SupplierSite saved = siteRepo.save(entity);
        return toResponse(saved);
    }

    public SupplierSiteResponse update(Long id, SupplierSiteRequest req) {
    	SupplierSite entity = siteRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Supplier site not found: " + id));
        apply(req, entity);
        SupplierSite saved = siteRepo.save(entity);
        return toResponse(saved);
    }

    public SupplierSiteResponse getById(Long id) {
    	SupplierSite entity = siteRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Supplier not found: " + id));
        return toResponse(entity);
    }

    public SupplierSiteResponse getByCode(String code) {
    	SupplierSite entity = siteRepo.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Supplier not found for code: " + code));
        return toResponse(entity);
    }

    public List<SupplierSiteResponse> getAllActive() {
        return siteRepo.findByIsActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void deactivate(Long id) {
    	SupplierSite entity = siteRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Supplier not found: " + id));
        entity.setIsActive(false);
        siteRepo.save(entity);
    }
    
    
    public Page<SupplierSiteResponse> searchSupplierSites(Long supplierId, String countryCode,
            Boolean active,
            Pageable pageable) {

		SupplierSiteFilter filter = SupplierSiteFilter.builder()
		.supplierId(supplierId)
		.countryCode(countryCode)
		.active(active)
		.build();
		
		var spec = SupplierSiteSpecifications.build(filter);
		
		return siteRepo.findAll(spec, pageable)
		.map(this::toResponse);
	}

    private void apply(SupplierSiteRequest req, SupplierSite entity) {
        entity.setSiteCode(req.getCode());
        entity.setName(req.getName());
        entity.setIsActive(req.getIsActive());
    }

    private SupplierSiteResponse toResponse(SupplierSite entity) {
    	SupplierSiteResponse dto = new SupplierSiteResponse();
        dto.setId(entity.getId());
        dto.setSiteCode(entity.getSiteCode());
        dto.setName(entity.getName());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAtUtc(entity.getCreatedAtUtc());
        dto.setUpdatedAtUtc(entity.getUpdatedAtUtc());
        return dto;
    }
}
