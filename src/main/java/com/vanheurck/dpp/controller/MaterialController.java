package com.vanheurck.dpp.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.vanheurck.dpp.dto.MaterialLotResponse;
import com.vanheurck.dpp.dto.MaterialRequest;
import com.vanheurck.dpp.dto.MaterialResponse;
import com.vanheurck.dpp.service.MaterialService;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Material API", description = "Operations related to Material")
@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    // Create material
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MaterialResponse create(@Valid @RequestBody MaterialRequest request) {
        return materialService.create(request);
    }

    // Update material
    @PutMapping("/{id}")
    public MaterialResponse update(@PathVariable Long id,
                                  @Valid @RequestBody MaterialRequest request) {
        return materialService.update(id, request);
    }

    // Get material by id
    @GetMapping("/{id}")
    public MaterialResponse getById(@PathVariable Long id) {
        return materialService.getById(id);
    }

    // Get by code
    @GetMapping("/by-code/{code}")
    public MaterialResponse getByCode(@PathVariable String code) {
        return materialService.getByCode(code);
    }

    // List active materials
    @GetMapping
    public List<MaterialResponse> getAllActive() {
        return materialService.getAllActive();
    }

    // Soft delete / deactivate
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
    	materialService.deactivate(id);
    }
    
 // material → lots
    @GetMapping("/{id}/lots")
    public List<MaterialLotResponse> getLots(@PathVariable Long id) {
        return materialService.getLotsForMaterial(id);
    }
}