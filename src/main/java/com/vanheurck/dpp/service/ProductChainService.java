package com.vanheurck.dpp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.vanheurck.dpp.dto.ProductChainResponse;
import com.vanheurck.dpp.entity.Company;
import com.vanheurck.dpp.entity.LotComponent;
import com.vanheurck.dpp.entity.Material;
import com.vanheurck.dpp.entity.MaterialLot;
import com.vanheurck.dpp.entity.Passport;
import com.vanheurck.dpp.entity.Product;
import com.vanheurck.dpp.entity.ProductLot;
import com.vanheurck.dpp.entity.Supplier;
import com.vanheurck.dpp.repo.CompanyRepository;
import com.vanheurck.dpp.repo.LotComponentRepository;
import com.vanheurck.dpp.repo.MaterialLotRepository;
import com.vanheurck.dpp.repo.MaterialRepository;
import com.vanheurck.dpp.repo.PassportRepository;
import com.vanheurck.dpp.repo.ProductLotRepository;
import com.vanheurck.dpp.repo.ProductRepository;
import com.vanheurck.dpp.repo.SupplierRepository;

import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductChainService {

    private final ProductRepository productRepo;
    private final PassportRepository passportRepo;
    private final ProductLotRepository productLotRepo;
    private final LotComponentRepository lotComponentRepo;
    private final MaterialLotRepository materialLotRepo;
    private final MaterialRepository materialRepo;
    private final SupplierRepository supplierRepo;
    private final CompanyRepository companyRepo;

    public ProductChainService(ProductRepository productRepo,
                               PassportRepository passportRepo,
                               ProductLotRepository productLotRepo,
                               LotComponentRepository lotComponentRepo,
                               MaterialLotRepository materialLotRepo,
                               MaterialRepository materialRepo,
                               SupplierRepository supplierRepo,
                               CompanyRepository companyRepo) {
        this.productRepo = productRepo;
        this.passportRepo = passportRepo;
        this.productLotRepo = productLotRepo;
        this.lotComponentRepo = lotComponentRepo;
        this.materialLotRepo = materialLotRepo;
        this.materialRepo = materialRepo;
        this.supplierRepo = supplierRepo;
        this.companyRepo = companyRepo;
    }

    public ProductChainResponse getChainForProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Product not found: " + productId));

        List<Passport> passports = passportRepo.findByProductId(productId);
        List<ProductLot> productLots = productLotRepo.findByProductId(productId);

        List<Long> productLotIds = productLots.stream()
                .map(ProductLot::getId)
                .toList();

        List<LotComponent> lotComponents = productLotIds.isEmpty()
                ? List.of()
                : lotComponentRepo.findByParentProductLotIdIn(productLotIds);

        // Collect material lot ids
        Set<Long> materialLotIds = lotComponents.stream()
                .map(LotComponent::getComponentMaterialLotId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, MaterialLot> materialLotMap = materialLotIds.isEmpty()
                ? Map.of()
                : materialLotRepo.findByIdIn(materialLotIds).stream()
                    .collect(Collectors.toMap(MaterialLot::getId, ml -> ml));

        // Material id → Material
        Set<Long> materialIds = materialLotMap.values().stream()
                .map(MaterialLot::getMaterialId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, Material> materialMap = materialIds.isEmpty()
                ? Map.of()
                : materialRepo.findAllById(materialIds).stream()
                    .collect(Collectors.toMap(Material::getId, m -> m));

        // Supplier & Company
        Set<Long> supplierIds = materialLotMap.values().stream()
                .map(MaterialLot::getSupplierId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, Supplier> supplierMap = supplierIds.isEmpty()
                ? Map.of()
                : supplierRepo.findAllById(supplierIds).stream()
                    .collect(Collectors.toMap(Supplier::getId, s -> s));

        Set<Long> companyIds = new HashSet<>();
        materialLotMap.values().forEach(ml -> {
            if (ml.getCompanyId() != null) companyIds.add(ml.getCompanyId());
        });
        supplierMap.values().forEach(s -> {
            if (s.getCompanyId() != null) companyIds.add(s.getCompanyId());
        });

        Map<Long, Company> companyMap = companyIds.isEmpty()
                ? Map.of()
                : companyRepo.findAllById(companyIds).stream()
                    .collect(Collectors.toMap(Company::getId, c -> c));

        // Build response DTO
        ProductChainResponse response = new ProductChainResponse();

        ProductChainResponse.ProductSummary p = new ProductChainResponse.ProductSummary();
        p.setId(product.getId());
        p.setSku(product.getSku());
        p.setName(product.getName());
        response.setProduct(p);

        response.setPassports(passports.stream().map(passport -> {
            ProductChainResponse.PassportSummary ps = new ProductChainResponse.PassportSummary();
            ps.setId(passport.getId());
            ps.setVersionNo(passport.getVersionNo());
            ps.setStatus(passport.getStatus().name());
            ps.setSerialNumber(passport.getSerialNumber());
            return ps;
        }).toList());

        response.setProductLots(productLots.stream().map(pl -> {
            ProductChainResponse.ProductLotSummary pls = new ProductChainResponse.ProductLotSummary();
            pls.setId(pl.getId());
            pls.setLotNo(pl.getLotNo());
            return pls;
        }).toList());

        List<ProductChainResponse.ComponentNode> componentNodes = new ArrayList<>();
        for (LotComponent lc : lotComponents) {
            if (lc.getComponentMaterialLotId() != null) {
                MaterialLot ml = materialLotMap.get(lc.getComponentMaterialLotId());
                if (ml == null) continue;

                Material mat = materialMap.get(ml.getMaterialId());
                Supplier sup = supplierMap.get(ml.getSupplierId());
                Company comp = ml.getCompanyId() != null ? companyMap.get(ml.getCompanyId()) :
                               (sup != null && sup.getCompanyId() != null ? companyMap.get(sup.getCompanyId()) : null);

                ProductChainResponse.ComponentNode node = new ProductChainResponse.ComponentNode();
                node.setParentProductLotId(lc.getParentProductLotId());
                node.setComponentType("MATERIAL");
                node.setComponentLotId(ml.getId());
                node.setMaterialCode(mat != null ? mat.getCode() : null);
                node.setMaterialName(mat != null ? mat.getName() : null);
                node.setSupplierName(sup != null ? sup.getName() : null);
                node.setCompanyName(comp != null ? comp.getName() : null);
                node.setQty(lc.getQty());
                node.setUom(lc.getUom());

                componentNodes.add(node);
            }
        }

        response.setComponents(componentNodes);

        return response;
    }
}