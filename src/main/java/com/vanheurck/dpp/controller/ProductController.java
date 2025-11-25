package com.vanheurck.dpp.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.vanheurck.dpp.dto.PassportResponse;
import com.vanheurck.dpp.dto.ProductRequest;
import com.vanheurck.dpp.dto.ProductResponse;
import com.vanheurck.dpp.service.PassportService;
import com.vanheurck.dpp.service.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Product API", description = "Operations related to Product")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final PassportService passportService;

    public ProductController(ProductService productService,
                             PassportService passportService) {
        this.productService = productService;
        this.passportService = passportService;
    }

    // Create product
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    // Update product
    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id,
                                  @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    // Get product by id
    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    // Get by SKU
    @GetMapping("/by-sku/{sku}")
    public ProductResponse getBySku(@PathVariable String sku) {
        return productService.getBySku(sku);
    }

    // List active products
    @GetMapping
    public List<ProductResponse> getAllActive() {
        return productService.getAllActive();
    }

    // Soft delete / deactivate
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        productService.deactivate(id);
    }

    // 🔗 Get passports for a product (reusing your existing service)
    @GetMapping("/{id}/passports")
    public List<PassportResponse> getPassportsForProduct(@PathVariable Long id) {
        return passportService.getByProduct(id);
    }
}