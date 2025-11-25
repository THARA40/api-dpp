package com.vanheurck.dpp.controller;

import org.springframework.web.bind.annotation.*;

import com.vanheurck.dpp.dto.SupplyChainNode;
import com.vanheurck.dpp.service.SupplyChainService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Supply Chain API", description = "Operations related to Supply Chain")
@RestController
@RequestMapping("/api/supply-chain")
public class SupplyChainController {

    private final SupplyChainService service;

    public SupplyChainController(SupplyChainService service) {
        this.service = service;
    }

    // GET /api/supply-chain/product-lot/1
    @GetMapping("/product-lot/{productLotId}")
    public SupplyChainNode getChainForProductLot(@PathVariable Long productLotId) {
        return service.getChainForProductLot(productLotId);
    }
    
    @GetMapping("/passport/{passportId}")
    public SupplyChainNode getChainForPassport(@PathVariable Long passportId) {
        return service.getChainForPassport(passportId);
    }
}
