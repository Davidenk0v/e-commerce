package com.ecommerce.cart.services.impl;

import com.ecommerce.cart.dto.response.CartDto;
import com.ecommerce.cart.dto.response.ProductDetailsDto;
import com.ecommerce.cart.entities.Cart;
import com.ecommerce.cart.entities.ProductDetails;
import com.ecommerce.cart.mapper.IDtoMapper;
import com.ecommerce.cart.repositories.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private IDtoMapper<Cart, CartDto> dtoMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addModelToCart_Success() {
        // Arrange
        String userId = "user123";
        ProductDetailsDto productDetails = new ProductDetailsDto(null,null,null, null, null);
        Cart cart = new Cart();
        cart.setProductsDetails(new ArrayList<>());
        CartDto cartDto = dtoMapper.entityToDto(cart);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(dtoMapper.entityToDto(any(Cart.class))).thenReturn(cartDto);

        // Act
        ResponseEntity<CartDto> response = cartService.addModelToCart(userId, productDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartRepository).save(cart);
    }

    @Test
    void addModelToCart_CartNotFound() {
        // Arrange
        String userId = "user123";
        ProductDetailsDto productDetails = new ProductDetailsDto(null,null,null, null, null);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cartService.addModelToCart(userId, productDetails));
    }


    @Test
    void updateModelQuantity_Success() {
        // Arrange
        Long modelId = 1L;
        String userId = "user123";
        Integer quantity = 2;
        Cart cart = new Cart();
        List<ProductDetails> productDetails = new ArrayList<>();
        ProductDetails product = new ProductDetails();
        product.setId(modelId);
        product.setQuantity(1);
        productDetails.add(product);
        cart.setProductsDetails(productDetails);
        CartDto cartDto = dtoMapper.entityToDto(cart);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(dtoMapper.entityToDto(any(Cart.class))).thenReturn(cartDto);

        // Act
        ResponseEntity<CartDto> response = cartService.updateModelQuantity(modelId, userId, quantity);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(quantity, cart.getProductsDetails().get(0).getQuantity());
        verify(cartRepository).save(cart);
    }

    @Test
    void updateModelQuantity_CartNotFound() {
        // Arrange
        Long modelId = 1L;
        String userId = "user123";
        Integer quantity = 2;
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cartService.updateModelQuantity(modelId, userId, quantity));
    }

    @Test
    void updateModelQuantity_ProductNotFound() {
        // Arrange
        Long modelId = 1L;
        String userId = "user123";
        Integer quantity = 2;
        Cart cart = new Cart();
        cart.setProductsDetails(new ArrayList<>());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cartService.updateModelQuantity(modelId, userId, quantity));
    }

    @Test
    void clearModelFromCart_Success() {
        // Arrange
        Long modelId = 1L;
        Long cartId = 1L;
        Cart cart = new Cart();
        List<ProductDetails> productDetails = new ArrayList<>();
        ProductDetails product = new ProductDetails();
        product.setId(modelId);
        productDetails.add(product);
        cart.setProductsDetails(productDetails);
        CartDto cartDto = dtoMapper.entityToDto(cart);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(dtoMapper.entityToDto(any(Cart.class))).thenReturn(cartDto);

        // Act
        ResponseEntity<CartDto> response = cartService.clearModelFromCart(modelId, cartId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(cart.getProductsDetails().isEmpty());
        verify(cartRepository).save(cart);
    }

    @Test
    void clearModelFromCart_CartNotFound() {
        // Arrange
        Long modelId = 1L;
        Long cartId = 1L;
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cartService.clearModelFromCart(modelId, cartId));
    }

    @Test
    void clearCart_Success() {
        // Arrange
        Long cartId = 1L;
        Cart cart = new Cart();
        cart.setProductsDetails(new ArrayList<>());
        CartDto cartDto = dtoMapper.entityToDto(cart);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(dtoMapper.entityToDto(any(Cart.class))).thenReturn(cartDto);

        // Act
        ResponseEntity<CartDto> response = cartService.clearCart(cartId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(cart.getProductsDetails().isEmpty());
        verify(cartRepository).save(cart);
    }

    @Test
    void clearCart_CartNotFound() {
        // Arrange
        Long cartId = 1L;
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cartService.clearCart(cartId));
    }

    @Test
    void getUserCart_Success() {
        // Arrange
        String userId = "user123";
        Cart cart = new Cart();
        CartDto cartDto = dtoMapper.entityToDto(cart);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(dtoMapper.entityToDto(any(Cart.class))).thenReturn(cartDto);

        // Act
        ResponseEntity<CartDto> response = cartService.getUserCart(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getUserCart_CartNotFound() {
        // Arrange
        String userId = "user123";
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cartService.getUserCart(userId));
    }

    @Test
    void newUserCart_Success() {
        // Arrange
        String userId = "user123";
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setProductsDetails(List.of());
        CartDto cartDto = dtoMapper.entityToDto(cart);

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(dtoMapper.entityToDto(any(Cart.class))).thenReturn(cartDto);

        // Act
        ResponseEntity<CartDto> response = cartService.newUserCart(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartRepository).save(any(Cart.class));
    }
}
