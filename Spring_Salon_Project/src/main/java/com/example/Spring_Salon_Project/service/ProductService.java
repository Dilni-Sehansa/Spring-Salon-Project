package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.ProductDTO;
import java.util.List;

public interface ProductService {
    ProductDTO saveProduct(ProductDTO productDTO);
    void updateProduct(ProductDTO productDTO);
    void deleteProduct(long productId);
    ProductDTO selectProduct(long productId);
    List<ProductDTO> getAllProducts();
    List<ProductDTO> getProductByStatus(String status);
    void updateProductStatus(Long productId, String status);
    List<ProductDTO> searchProductsByName(String keyword);
    List<ProductDTO> getProductsByCategory(Long categoryId);
    List<ProductDTO> getProductsBySupplier(Long supplierId);
    boolean deductStock(Long productId, Integer qty);
    List<ProductDTO> getLowStockProducts(Integer thresholdQty);
}
