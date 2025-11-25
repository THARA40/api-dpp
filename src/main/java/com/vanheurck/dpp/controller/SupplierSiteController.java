package com.vanheurck.dpp.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.vanheurck.dpp.dto.SupplierSiteRequest;
import com.vanheurck.dpp.dto.SupplierSiteResponse;
import com.vanheurck.dpp.service.SupplierSiteService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Supplier Site API", description = "Operations related to Supplier Site")
@RestController
@RequestMapping("/api/supplier-sites")
public class SupplierSiteController {

    private final SupplierSiteService supplierSiteService;

    public SupplierSiteController(SupplierSiteService supplierSiteService) {
        this.supplierSiteService = supplierSiteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierSiteResponse create(@Valid @RequestBody SupplierSiteRequest request) {
        return supplierSiteService.create(request);
    }

    @PutMapping("/{id}")
    public SupplierSiteResponse update(@PathVariable Long id,
                                   @Valid @RequestBody SupplierSiteRequest request) {
        return supplierSiteService.update(id, request);
    }

    @GetMapping("/{id}")
    public SupplierSiteResponse getById(@PathVariable Long id) {
        return supplierSiteService.getById(id);
    }

    @GetMapping("/by-code/{code}")
    public SupplierSiteResponse getByCode(@PathVariable String code) {
        return supplierSiteService.getByCode(code);
    }

    @GetMapping("/active")
    public List<SupplierSiteResponse> getAllActive() {
        return supplierSiteService.getAllActive();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
    	supplierSiteService.deactivate(id);
    }
    
    @GetMapping
    public Page<SupplierSiteResponse> listSupplierSites(
    		@RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Boolean active,
            Pageable pageable
    ) {
        return supplierSiteService.searchSupplierSites(supplierId, country, active, pageable);
    }
}
