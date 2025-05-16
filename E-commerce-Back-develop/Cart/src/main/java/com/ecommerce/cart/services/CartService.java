package com.ecommerce.cart.services;

import com.ecommerce.cart.dto.response.CartDto;
import com.ecommerce.cart.dto.response.ProductDetailsDto;
import com.ecommerce.cart.entities.ProductDetails;
import org.springframework.http.ResponseEntity;

public interface CartService {

    ResponseEntity<CartDto> addModelToCart(String userId, ProductDetailsDto productDetails);

    ResponseEntity<CartDto> updateModelQuantity(Long modelId, String userId, Integer quantity);

    ResponseEntity<CartDto> clearModelFromCart(Long modelId, Long cartId);

    ResponseEntity<CartDto> clearCart(Long cartId);

    ResponseEntity<CartDto> getUserCart(String userId);

    ResponseEntity<CartDto> newUserCart(String userId);
}
