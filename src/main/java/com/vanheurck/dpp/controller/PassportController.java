package com.vanheurck.dpp.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.vanheurck.dpp.dto.PassportRequest;
import com.vanheurck.dpp.dto.PassportResponse;
import com.vanheurck.dpp.service.PassportService;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Product Passport API", description = "Operations related to Digital Product Passports")
@RestController
@RequestMapping("/api/passports")
public class PassportController {

    private final PassportService service;

    public PassportController(PassportService service) {
        this.service = service;
    }

    // Create passport
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PassportResponse create(@Valid @RequestBody PassportRequest request) {
        return service.create(request);
    }

    // Update passport by id
    @PutMapping("/{id}")
    public PassportResponse update(@PathVariable Long id,
                                   @Valid @RequestBody PassportRequest request) {
        return service.update(id, request);
    }

    // Get passport by id
    @GetMapping("/{id}")
    public PassportResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // Get passport by product + version
    @GetMapping("/by-product/{productId}/version/{versionNo}")
    public PassportResponse getByProductAndVersion(@PathVariable Long productId,
                                                   @PathVariable Integer versionNo) {
        return service.getByProductAndVersion(productId, versionNo);
    }

    // List passports for a product
    @GetMapping("/by-product/{productId}")
    public List<PassportResponse> getByProduct(@PathVariable Long productId) {
        return service.getByProduct(productId);
    }
}
