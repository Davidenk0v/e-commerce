package com.ecommerce.cart.services;

import com.ecommerce.cart.dto.response.ProductDetailsDto;
import org.springframework.http.ResponseEntity;

public interface ProductDetailsService {
    ResponseEntity<ProductDetailsDto> getProductDetails(Long productId);

    ResponseEntity<ProductDetailsDto> saveProductDetails(ProductDetailsDto productDetails);

    ResponseEntity<ProductDetailsDto> updateProductDetails(ProductDetailsDto productDetails);

    ResponseEntity<ProductDetailsDto> deleteProductDetails(Long productId);
}
