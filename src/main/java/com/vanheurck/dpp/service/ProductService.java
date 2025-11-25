package com.vanheurck.dpp.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.vanheurck.dpp.dto.ProductRequest;
import com.vanheurck.dpp.dto.ProductResponse;
import com.vanheurck.dpp.entity.Product;
import com.vanheurck.dpp.repo.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductResponse create(ProductRequest req) {
        Product entity = new Product();
        apply(req, entity);
        Product saved = repository.save(entity);
        return toResponse(saved);
    }

    public ProductResponse update(Long id, ProductRequest req) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found: " + id));
        apply(req, entity);
        Product saved = repository.save(entity);
        return toResponse(saved);
    }

    public ProductResponse getById(Long id) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found: " + id));
        return toResponse(entity);
    }

    public ProductResponse getBySku(String sku) {
        Product entity = repository.findBySku(sku)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found for sku: " + sku));
        return toResponse(entity);
    }

    public List<ProductResponse> getAllActive() {
        return repository.findByIsActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void deactivate(Long id) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found: " + id));
        entity.setIsActive(false);
        repository.save(entity);
    }

    private void apply(ProductRequest req, Product entity) {
        entity.setSku(req.getSku());
        entity.setName(req.getName());
        entity.setCategory(req.getCategory());
        entity.setUnitOfMeasure(req.getUnitOfMeasure());
        entity.setIsActive(req.getIsActive());
    }

    private ProductResponse toResponse(Product entity) {
        ProductResponse dto = new ProductResponse();
        dto.setId(entity.getId());
        dto.setSku(entity.getSku());
        dto.setName(entity.getName());
        dto.setCategory(entity.getCategory());
        dto.setUnitOfMeasure(entity.getUnitOfMeasure());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAtUtc(entity.getCreatedAtUtc());
        dto.setUpdatedAtUtc(entity.getUpdatedAtUtc());
        return dto;
    }
}
