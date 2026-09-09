package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.AuditLogDTO;
import com.example.Spring_Salon_Project.dto.ProductDTO;
import com.example.Spring_Salon_Project.entity.Category;
import com.example.Spring_Salon_Project.entity.Product;
import com.example.Spring_Salon_Project.entity.Supplier;
import com.example.Spring_Salon_Project.enumiration.ProductStatus;
import com.example.Spring_Salon_Project.exception.CustomerException;
import com.example.Spring_Salon_Project.repository.CategoryRepository;
import com.example.Spring_Salon_Project.repository.ProductRepository;
import com.example.Spring_Salon_Project.repository.SupplierRepository;
import com.example.Spring_Salon_Project.service.AuditLogService;
import com.example.Spring_Salon_Project.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final AuditLogService auditLogService;

    @Override
    public ProductDTO saveProduct(ProductDTO productDTO) {
        log.info("Execute method saveProduct");
        try{
            Product product = new Product();
            product.setProductName(productDTO.getProductName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setQtyOnHand(productDTO.getQtyOnHand());
            product.setProductImage(productDTO.getProductImage());
            product.setProductStatus(productDTO.getProductStatus() != null ? productDTO.getProductStatus() : ProductStatus.ACTIVE);

            if (productDTO.getCategoryId() != null) {
                Optional<Category> optionalCategory = categoryRepository.findById(productDTO.getCategoryId());
                if (optionalCategory.isPresent()) {
                    product.setCategory(optionalCategory.get());
                } else {
                    throw new CustomerException(404, "Category not found");
                }
            }

            if (productDTO.getSupplierId() != null) {
                Optional<Supplier> optionalSupplier = supplierRepository.findById(productDTO.getSupplierId());
                if (optionalSupplier.isPresent()) {
                    product.setSupplier(optionalSupplier.get());
                } else {
                    throw new CustomerException(404, "Supplier not found");
                }
            }

            Product savedProduct = productRepository.save(product);
            log.info("Product saved successfully");

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("CREATE");
            logDTO.setEntityName("PRODUCT");
            logDTO.setEntityId(savedProduct.getProductId());
            logDTO.setPerformedBy("admin");
            logDTO.setDetails("New product created: " + savedProduct.getProductName());
            auditLogService.saveAuditLog(logDTO);

            Long saveCategoryId = (savedProduct.getCategory() != null) ? savedProduct.getCategory().getCategoryId() : null;
            String saveCategoryName = (savedProduct.getCategory() != null) ? savedProduct.getCategory().getCategoryName() : null;
            Long saveSupplierId = (savedProduct.getSupplier() != null) ? savedProduct.getSupplier().getSupplierId() : null;
            String saveSupplierName = (savedProduct.getSupplier() != null) ? savedProduct.getSupplier().getSupplierName() : null;
            return new ProductDTO(savedProduct.getProductId(), savedProduct.getProductName(), savedProduct.getDescription(), savedProduct.getPrice(), savedProduct.getQtyOnHand(), savedProduct.getProductImage(), saveCategoryId, saveCategoryName, saveSupplierId, saveSupplierName, savedProduct.getProductStatus());
        }catch(Exception ex) {
            log.error("Error saving product: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void updateProduct(ProductDTO productDTO) {
        Optional<Product> optionalProduct = productRepository.findById(productDTO.getProductId());

        if(optionalProduct.isEmpty())
            throw new CustomerException(404, "Product not found");

        Product product = optionalProduct.get();
        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQtyOnHand(productDTO.getQtyOnHand());

        if (productDTO.getProductImage() != null && !productDTO.getProductImage().isEmpty()) {
            product.setProductImage(productDTO.getProductImage());
        }

        if(productDTO.getProductStatus() != null)
            product.setProductStatus(productDTO.getProductStatus());

        if(productDTO.getCategoryId() != null){
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new CustomerException(404, "Category not found"));
            product.setCategory(category);
        }
        if(productDTO.getSupplierId() != null){
            Supplier supplier = supplierRepository.findById(productDTO.getSupplierId())
                    .orElseThrow(() -> new CustomerException(404, "Supplier not found"));
            product.setSupplier(supplier);
        }

        productRepository.save(product);
        log.info("Product updated successfully");

        AuditLogDTO logDTO = new AuditLogDTO();
        logDTO.setAction("UPDATE");
        logDTO.setEntityName("PRODUCT");
        logDTO.setEntityId(product.getProductId());
        logDTO.setPerformedBy("admin");
        logDTO.setDetails("Product updated: " + product.getProductName());
        auditLogService.saveAuditLog(logDTO);

    }

    @Override
    public void deleteProduct(long productId) {
        log.info("Execute method deleteProduct() productId: {}", productId);

        try {
            Optional<Product> optionalProduct = productRepository.findById(productId);

            if(optionalProduct.isEmpty() || optionalProduct.get().getProductStatus()==ProductStatus.INACTIVE) {
                throw new CustomerException(404, "Product not found");
            }
            Product product = optionalProduct.get();
            product.setProductStatus(ProductStatus.INACTIVE);
            productRepository.save(product);

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("DELETE");
            logDTO.setEntityName("PRODUCT");
            logDTO.setEntityId(product.getProductId());
            logDTO.setPerformedBy("admin");
            logDTO.setDetails("Product soft-deleted: " + product.getProductName());
            auditLogService.saveAuditLog(logDTO);
        }catch(Exception ex) {
            log.error("Error deleting product: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ProductDTO selectProduct(long productId) {
        Optional<ProductDTO> optionalProductDTO = productRepository.selectProduct(productId);

        if (optionalProductDTO.isPresent()) {
            return optionalProductDTO.get();
        } else {
            throw new CustomerException(404, "Product not found");
        }
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public List<ProductDTO> getProductByStatus(String status) {
        log.info("Execute method getProductByStatus() status: {}", status);

        try {
            ProductStatus productStatus = ProductStatus.valueOf(status.toUpperCase());
            List<ProductDTO> productsList = productRepository.getProductsByStatus(productStatus);

            if (productsList.isEmpty()) {
                throw new CustomerException(404, "No products found for status: " + status);
            }
            return productsList;
        } catch (IllegalArgumentException e) {
            throw new CustomerException(400, "Invalid status string provided: " + status);
        }
    }

    @Override
    public void updateProductStatus(Long productId, String status) {
        log.info("Execute method updateProductStatus for ID: {} to Status: {}", productId, status);

        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isEmpty()) {
            throw new CustomerException(404, "Product not found");
        }

        try {
            Product product = optionalProduct.get();
            product.setProductStatus(ProductStatus.valueOf(status.toUpperCase()));
            productRepository.save(product);
            log.info("Product status updated successfully");

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("UPDATE");
            logDTO.setEntityName("PRODUCT");
            logDTO.setEntityId(product.getProductId());
            logDTO.setPerformedBy("admin");
            logDTO.setDetails("Product status changed to: " + status);
            auditLogService.saveAuditLog(logDTO);

        } catch (IllegalArgumentException e) {
            throw new CustomerException(400, "Invalid status: " + status);
        }
    }

    @Override
    public List<ProductDTO> searchProductsByName(String keyword) {
        log.info("Execute method searchProductsByName for keyword: {}", keyword);

        List<ProductDTO> productList = productRepository.findByProductName(keyword);

        if (productList.isEmpty()) {
            throw new CustomerException(404, "Product not found");
        }
        return productList;

    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        log.info("Execute method getProductsByCategory() categoryId: {}", categoryId);

        List<ProductDTO> productsList = productRepository.getProductsByCategory(categoryId);

        if (productsList.isEmpty()) {
            throw new CustomerException(404, "Products not found for category ID: " + categoryId);
        }
        return productsList;
    }

    @Override
    public List<ProductDTO> getProductsBySupplier(Long supplierId) {
        log.info("Execute method getProductsBySupplier() supplierId: {}", supplierId);

        List<ProductDTO> productsList = productRepository.getProductsBySupplier(supplierId);

        if (productsList.isEmpty()) {
            throw new CustomerException(404, "Products not found for supplier ID: " + supplierId);
        }
        return productsList;
    }

    @Override
    public boolean deductStock(Long productId, Integer qty) {
        log.info("Execute deductStock for product ID: {}, Qty: {}", productId, qty);

        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            if (product.getQtyOnHand() >= qty) {
                product.setQtyOnHand(product.getQtyOnHand() - qty);

                if (product.getQtyOnHand() == 0) {
                    product.setProductStatus(ProductStatus.OUT_OF_STOCK);
                }

                productRepository.save(product);
                return true;
            } else {
                return false;
            }
        } else {
            throw new CustomerException(404, "Product not found");
        }
    }

    @Override
    public List<ProductDTO> getLowStockProducts(Integer thresholdQty) {
        log.info("Execute getLowStockProducts for threshold: {}", thresholdQty);
        return productRepository.getLowStockProducts(thresholdQty);
    }
}
/*
* private Long productId;
    private String productName;
    private String description;
    private Double price;
    private Integer qtyOnHand;
    private String productImage;
    private Long categoryId;
    private String categoryName;
    private Long supplierId;
    private String supplierName;
    private ProductStatus productStatus;*/
