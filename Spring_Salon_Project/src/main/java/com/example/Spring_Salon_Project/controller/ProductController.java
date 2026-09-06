package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.AppointmentDTO;
import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.dto.ProductDTO;
import com.example.Spring_Salon_Project.service.CategoryService;
import com.example.Spring_Salon_Project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(value = "/save-product", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO saveProduct = productService.saveProduct(productDTO);
        return new CommonResponse(0, saveProduct, "Product Saved Successfully");
    }

    @GetMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllProducts() {
        List<ProductDTO> productDTOS = productService.getAllProducts();
        return new CommonResponse(0, productDTOS, "Product Loaded Successfully");
    }

    @GetMapping(value = "/select-product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getProductById(@PathVariable long productId) {
        ProductDTO productDTO = productService.selectProduct(productId);
        return new CommonResponse(0, productDTO, "Product Loaded Successfully");
    }

    @PutMapping(value = "/update-product", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateProduct(@RequestBody ProductDTO productDTO) {
        productService.updateProduct(productDTO);
        return new CommonResponse(0, "Product Updated Successfully");
    }

    @DeleteMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteProduct(@PathVariable long productId) {
        productService.deleteProduct(productId);
        return new CommonResponse(0, "Product Deleted Successfully");
    }

    @GetMapping(value = "/by-status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getProductStatus(@PathVariable String status) {
        List<ProductDTO> productDTOS = productService.getProductByStatus(status);
        return new CommonResponse(0, productDTOS, "Product Loaded Successfully");
    }

    @GetMapping(value = "/by-category/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getProductByCategoryId(@PathVariable Long categoryId) {
        List<ProductDTO> productDTOS = productService.getProductsByCategory(categoryId);
        return new CommonResponse(0, productDTOS, "Product Loaded Successfully");
    }

    @GetMapping(value = "/by-supplier/{supplierId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getProductBySupplierId(@PathVariable Long supplierId) {
        List<ProductDTO> productDTOS = productService.getProductsBySupplier(supplierId);
        return new CommonResponse(0, productDTOS, "Product Loaded Successfully");
    }

    @PatchMapping(value = "/update-status/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateProductStatus(@PathVariable Long productId, @RequestParam String status) {
        productService.updateProductStatus(productId, status);
        return new CommonResponse(0, "Product Status Updated Successfully");
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse searchProductsByName(@RequestParam String keyword) {
        List<ProductDTO> productDTOS = productService.searchProductsByName(keyword);
        return new CommonResponse(0, productDTOS, "Products Found Successfully");
    }

    @PostMapping(value = "/deduct-stock", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deductStock(@RequestParam Long productId, @RequestParam Integer qty) {
        boolean isDeducted = productService.deductStock(productId, qty);
        if (isDeducted) {
            return new CommonResponse(0, true, "Stock Deducted Successfully");
        } else {
            return new CommonResponse(1, false, "Insufficient Stock");
        }
    }

    @GetMapping(value = "/low-stock", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getLowStockProducts(@RequestParam Integer thresholdQty) {
        List<ProductDTO> productDTOS = productService.getLowStockProducts(thresholdQty);
        return new CommonResponse(0, productDTOS, "Low Stock Products Loaded Successfully");
    }




}
