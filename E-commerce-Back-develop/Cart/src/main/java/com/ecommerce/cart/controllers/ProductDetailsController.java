package com.ecommerce.cart.controllers;

import com.ecommerce.cart.dto.response.ProductDetailsDto;
import com.ecommerce.cart.entities.ProductDetails;
import com.ecommerce.cart.services.ProductDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart/product-details")
@RequiredArgsConstructor
public class ProductDetailsController {

    private final ProductDetailsService productDetailsService;


    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailsDto> getProductDetails(@PathVariable Long productId) {
        return productDetailsService.getProductDetails(productId);
    }

    @PutMapping("/")
    public ResponseEntity<ProductDetailsDto> updateProductDetails(@RequestBody ProductDetailsDto productDetails) {
        return productDetailsService.updateProductDetails(productDetails);
    }

    @PostMapping("/")
    public ResponseEntity<ProductDetailsDto> saveProductDetails(@RequestBody ProductDetailsDto productDetails) {
        return productDetailsService.saveProductDetails(productDetails);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductDetailsDto> deleteProductDetails(@PathVariable Long productId) {
        return productDetailsService.deleteProductDetails(productId);
    }

}
