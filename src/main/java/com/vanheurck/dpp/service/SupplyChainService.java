package com.vanheurck.dpp.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.vanheurck.dpp.dto.SupplyChainNode;
import com.vanheurck.dpp.entity.LotComponent;
import com.vanheurck.dpp.entity.MaterialLot;
import com.vanheurck.dpp.entity.Passport;
import com.vanheurck.dpp.entity.ProductLot;
import com.vanheurck.dpp.repo.LotComponentRepository;
import com.vanheurck.dpp.repo.MaterialLotRepository;
import com.vanheurck.dpp.repo.PassportRepository;
import com.vanheurck.dpp.repo.ProductLotRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class SupplyChainService {

	private final PassportRepository passportRepository;
    private final ProductLotRepository productLotRepository;
    private final MaterialLotRepository materialLotRepository;
    private final LotComponentRepository lotComponentRepository;

    public SupplyChainService(PassportRepository passportRepository,
    							ProductLotRepository productLotRepository,
    							MaterialLotRepository materialLotRepository,
    							LotComponentRepository lotComponentRepository) {
    	 this.passportRepository = passportRepository;
        this.productLotRepository = productLotRepository;
        this.materialLotRepository = materialLotRepository;
        this.lotComponentRepository = lotComponentRepository;
    }

    public SupplyChainNode getChainForProductLot(Long productLotId) {
        ProductLot rootLot = productLotRepository.findById(productLotId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product lot not found: " + productLotId));

        SupplyChainNode root = toProductLotNode(rootLot);

        // to avoid infinite loops if data is bad (cycles)
        Set<String> visited = new HashSet<>();
        visited.add("P:" + rootLot.getId());

        populateChildrenForProductLot(rootLot, root, visited);

        return root;
    }
    
    /**
     * Multi-tier (recursive) supply chain for a passport:
     * Passport -> Product -> ProductLot -> (ProductLots/MaterialLots recursively via lot_component)
     */
    public SupplyChainNode getChainForPassport(Long passportId) {

        Passport passport = passportRepository.findById(passportId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Passport not found: " + passportId));

        // For now: pick the latest product lot for that product.
        ProductLot rootLot = productLotRepository
                .findFirstByProductIdOrderByIdDesc(passport.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No product lot for productId=" + passport.getProductId()));

        // Keep visited to avoid infinite loops in weird data
        Set<String> visited = new HashSet<>();

        return buildProductLotNode(rootLot, visited);
    }
    
    private SupplyChainNode buildProductLotNode(ProductLot lot, Set<String> visited) {
        String key = "P-" + lot.getId();
        if (!visited.add(key)) {
            // already processed; avoid cycles
            SupplyChainNode node = new SupplyChainNode();
            node.setLotType("PRODUCT");
            node.setLotId(lot.getId());
            node.setLotNo(lot.getLotNo());
            node.setProductId(lot.getProductId());
            node.setProductName(lot.getProduct() != null ? lot.getProduct().getName() : null);
            node.setProductSku(lot.getProduct() != null ? lot.getProduct().getSku() : null);
            return node;
        }

        SupplyChainNode node = new SupplyChainNode();
        node.setLotType("PRODUCT");
        node.setLotId(lot.getId());
        node.setLotNo(lot.getLotNo());
        node.setProductId(lot.getProductId());
        if (lot.getProduct() != null) {
            node.setProductName(lot.getProduct().getName());
            node.setProductSku(lot.getProduct().getSku());
        }
        if (lot.getCompany() != null) {
            node.setCompanyId(lot.getCompany().getId());
            node.setCompanyName(lot.getCompany().getName());
        }
        if (lot.getSite() != null && lot.getSite().getSupplier() != null) {
            node.setSupplierId(lot.getSite().getSupplier().getId());
            node.setSupplierName(lot.getSite().getSupplier().getName());
        }

        // Children from lot_component parent_product_lot_id
        List<LotComponent> components = lotComponentRepository.findByParentProductLotId(lot.getId());
        List<SupplyChainNode> childNodes = new ArrayList<>();

        for (LotComponent lc : components) {
            if (lc.getComponentProductLotId() != null) {
                ProductLot childProductLot = productLotRepository
                        .findById(lc.getComponentProductLotId())
                        .orElse(null);
                if (childProductLot != null) {
                    childNodes.add(buildProductLotNode(childProductLot, visited));
                }
            } else if (lc.getComponentMaterialLotId() != null) {
                MaterialLot childMaterialLot = materialLotRepository
                        .findById(lc.getComponentMaterialLotId())
                        .orElse(null);
                if (childMaterialLot != null) {
                    childNodes.add(buildMaterialLotNode(childMaterialLot, visited));
                }
            }
        }

        node.setComponents(childNodes);
        return node;
    }
    
    private SupplyChainNode buildMaterialLotNode(MaterialLot lot, Set<String> visited) {
        String key = "M-" + lot.getId();
        if (!visited.add(key)) {
            SupplyChainNode node = new SupplyChainNode();
            node.setLotType("MATERIAL");
            node.setLotId(lot.getId());
            node.setLotNo(lot.getLotNo());
            node.setMaterialId(lot.getMaterialId());
            node.setMaterialName(lot.getMaterial() != null ? lot.getMaterial().getName() : null);
            node.setMaterialCode(lot.getMaterial() != null ? lot.getMaterial().getCode() : null);
            return node;
        }

        SupplyChainNode node = new SupplyChainNode();
        node.setLotType("MATERIAL");
        node.setLotId(lot.getId());
        node.setLotNo(lot.getLotNo());
        node.setMaterialId(lot.getMaterialId());
        if (lot.getMaterial() != null) {
            node.setMaterialName(lot.getMaterial().getName());
            node.setMaterialCode(lot.getMaterial().getCode());
        }
        if (lot.getCompany() != null) {
            node.setCompanyId(lot.getCompany().getId());
            node.setCompanyName(lot.getCompany().getName());
        }
        if (lot.getSupplier() != null) {
            node.setSupplierId(lot.getSupplier().getId());
            node.setSupplierName(lot.getSupplier().getName());
        }

        // Children from lot_component parent_material_lot_id (if you allow that)
        List<LotComponent> components = lotComponentRepository.findByParentMaterialLotId(lot.getId());
        List<SupplyChainNode> childNodes = new ArrayList<>();

        for (LotComponent lc : components) {
            if (lc.getComponentProductLotId() != null) {
                ProductLot childProductLot = productLotRepository
                        .findById(lc.getComponentProductLotId())
                        .orElse(null);
                if (childProductLot != null) {
                    childNodes.add(buildProductLotNode(childProductLot, visited));
                }
            } else if (lc.getComponentMaterialLotId() != null) {
                MaterialLot childMaterialLot = materialLotRepository
                        .findById(lc.getComponentMaterialLotId())
                        .orElse(null);
                if (childMaterialLot != null) {
                    childNodes.add(buildMaterialLotNode(childMaterialLot, visited));
                }
            }
        }

        node.setComponents(childNodes);
        return node;
    }

    private void populateChildrenForProductLot(ProductLot parentLot,
                                               SupplyChainNode parentNode,
                                               Set<String> visited) {
        List<LotComponent> components =
                lotComponentRepository.findByParentProductLot(parentLot);

        for (LotComponent lc : components) {
            if (lc.getComponentProductLot() != null) {
                ProductLot childPl = lc.getComponentProductLot();
                String key = "P:" + childPl.getId();
                if (visited.add(key)) {
                    SupplyChainNode childNode = toProductLotNode(childPl);
                    childNode.setQty(lc.getQty());
                    childNode.setUom(lc.getUom());
                    parentNode.getChildren().add(childNode);
                    populateChildrenForProductLot(childPl, childNode, visited);
                }
            } else if (lc.getComponentMaterialLot() != null) {
                MaterialLot childMl = lc.getComponentMaterialLot();
                String key = "M:" + childMl.getId();
                if (visited.add(key)) {
                    SupplyChainNode childNode = toMaterialLotNode(childMl);
                    childNode.setQty(lc.getQty());
                    childNode.setUom(lc.getUom());
                    parentNode.getChildren().add(childNode);
                    populateChildrenForMaterialLot(childMl, childNode, visited);
                }
            }
        }
    }

    private void populateChildrenForMaterialLot(MaterialLot parentLot,
                                                SupplyChainNode parentNode,
                                                Set<String> visited) {
        List<LotComponent> components =
                lotComponentRepository.findByParentMaterialLot(parentLot);

        for (LotComponent lc : components) {
            if (lc.getComponentProductLot() != null) {
                ProductLot childPl = lc.getComponentProductLot();
                String key = "P:" + childPl.getId();
                if (visited.add(key)) {
                    SupplyChainNode childNode = toProductLotNode(childPl);
                    childNode.setQty(lc.getQty());
                    childNode.setUom(lc.getUom());
                    parentNode.getChildren().add(childNode);
                    populateChildrenForProductLot(childPl, childNode, visited);
                }
            } else if (lc.getComponentMaterialLot() != null) {
                MaterialLot childMl = lc.getComponentMaterialLot();
                String key = "M:" + childMl.getId();
                if (visited.add(key)) {
                    SupplyChainNode childNode = toMaterialLotNode(childMl);
                    childNode.setQty(lc.getQty());
                    childNode.setUom(lc.getUom());
                    parentNode.getChildren().add(childNode);
                    populateChildrenForMaterialLot(childMl, childNode, visited);
                }
            }
        }
    }

    private SupplyChainNode toProductLotNode(ProductLot lot) {
        SupplyChainNode node = new SupplyChainNode();
        node.setNodeType("PRODUCT_LOT");
        node.setProductLotId(lot.getId());
        node.setLotNo(lot.getLotNo());
        node.setProductId(lot.getProductId());
        node.setCompanyId(lot.getCompanyId());
        node.setSiteId(lot.getSiteId());
        // TODO: optionally lookup productName, companyName, siteName via other repos.
        return node;
    }

    private SupplyChainNode toMaterialLotNode(MaterialLot lot) {
        SupplyChainNode node = new SupplyChainNode();
        node.setNodeType("MATERIAL_LOT");
        node.setMaterialLotId(lot.getId());
        node.setLotNo(lot.getLotNo());
        node.setMaterialId(lot.getMaterialId());
        node.setSupplierId(lot.getSupplierId());
        node.setCompanyId(lot.getCompanyId());
        node.setSiteId(lot.getSiteId());
        // TODO: optionally lookup materialName, supplierName, companyName, siteName.
        return node;
    }
}
