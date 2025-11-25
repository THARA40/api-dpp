package com.vanheurck.dpp.controller;

import org.springframework.web.bind.annotation.*;

import com.vanheurck.dpp.dto.ProductChainResponse;
import com.vanheurck.dpp.service.ProductChainService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product Chain API", description = "Operations related to Product Chain")
@RestController
@RequestMapping("/api/products")
public class ProductChainController {

    private final ProductChainService service;

    public ProductChainController(ProductChainService service) {
        this.service = service;
    }

    @GetMapping("/{productId}/chain")
    public ProductChainResponse getProductChain(@PathVariable Long productId) {
        return service.getChainForProduct(productId);
    }
}
