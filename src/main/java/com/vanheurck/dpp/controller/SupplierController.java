package com.vanheurck.dpp.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.vanheurck.dpp.dto.CertificationResponse;
import com.vanheurck.dpp.dto.SupplierRequest;
import com.vanheurck.dpp.dto.SupplierResponse;
import com.vanheurck.dpp.dto.SupplierSiteResponse;
import com.vanheurck.dpp.service.SupplierService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Supplier API", description = "Operations related to Supplier")
@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierResponse create(@Valid @RequestBody SupplierRequest request) {
        return supplierService.create(request);
    }

    @PutMapping("/{id}")
    public SupplierResponse update(@PathVariable Long id,
                                   @Valid @RequestBody SupplierRequest request) {
        return supplierService.update(id, request);
    }

    @GetMapping("/{id}")
    public SupplierResponse getById(@PathVariable Long id) {
        return supplierService.getById(id);
    }

    @GetMapping("/by-code/{code}")
    public SupplierResponse getByCode(@PathVariable String code) {
        return supplierService.getByCode(code);
    }

    @GetMapping
    public List<SupplierResponse> getAllActive() {
        return supplierService.getAllActive();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        supplierService.deactivate(id);
    }
    
    // supplier → sites
    @GetMapping("/{id}/sites")
    public List<SupplierSiteResponse> getSites(@PathVariable Long id) {
        return supplierService.getSites(id);
    }

    // supplier → certifications
    @GetMapping("/{id}/certifications")
    public List<CertificationResponse> getCertifications(@PathVariable Long id) {
        return supplierService.getCertifications(id);
    }
    
    @GetMapping
    public Page<SupplierResponse> listSuppliers(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Boolean active,
            Pageable pageable
    ) {
        return supplierService.searchSuppliers(country, active, pageable);
    }
}
