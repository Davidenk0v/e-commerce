package com.ecommerce.cart.services.impl;

import com.ecommerce.cart.dto.response.ProductDetailsDto;
import com.ecommerce.cart.entities.ProductDetails;
import org.junit.jupiter.api.Test;


import com.ecommerce.cart.entities.Cart;

import com.ecommerce.cart.mapper.IDtoMapper;
import com.ecommerce.cart.repositories.ProductDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductDetailsServiceImplTest {
    /*
        @Mock
        private ProductDetailsRepository productDetailsRepository;

        @Mock
        private IDtoMapper<ProductDetails, ProductDetailsDto> dtoMapper;

        @InjectMocks
        private ProductDetailsServiceImpl productDetailsService;

        private ProductDetails productDetails;
        private ProductDetailsDto productDetailsDto;
        private Cart cart;

        @BeforeEach
        void setUp() {
            cart = Cart.builder()
                    .id(1L)
                    .userId("user123")
                    .build();

            productDetails = ProductDetails.builder()
                    .id(1L)
                    .modelId(100L)
                    .quantity(2)
                    .cart(cart)
                    .build();

            productDetailsDto = ProductDetailsDto.builder()
                    .id(1L)
                    .modelId(100L)
                    .quantity(2)
                    .cartId(1L)
                    .build();
        }

        @Test
        void should_get_product_details() {
            // given
            when(productDetailsRepository.findById(1L)).thenReturn(Optional.of(productDetails));
            when(dtoMapper.entityToDto(productDetails)).thenReturn(productDetailsDto);

            // when
            ResponseEntity<ProductDetailsDto> response = productDetailsService.getProductDetails(1L);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(productDetailsDto);
            verify(productDetailsRepository).findById(1L);
            verify(dtoMapper).entityToDto(productDetails);
        }

        @Test
        void should_throw_exception_when_product_details_not_found() {
            // given
            when(productDetailsRepository.findById(999L)).thenReturn(Optional.empty());

            // when, then
            assertThrows(IllegalArgumentException.class, () -> productDetailsService.getProductDetails(999L));
            verify(productDetailsRepository).findById(999L);
            verify(dtoMapper, never()).entityToDto(any());
        }

        @Test
        void should_save_product_details() {
            // given
            when(productDetailsRepository.save(productDetails)).thenReturn(productDetails);
            when(dtoMapper.entityToDto(productDetails)).thenReturn(productDetailsDto);

            // when
            ResponseEntity<ProductDetailsDto> response = productDetailsService.saveProductDetails(productDetailsDto);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isEqualTo(productDetailsDto);
            verify(productDetailsRepository).save(productDetails);
            verify(dtoMapper).entityToDto(productDetails);
        }


        @Test
        void should_throw_exception_when_error_saving_product_details() {
            // given
            when(productDetailsRepository.save(any())).thenThrow(new RuntimeException("Database error"));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> productDetailsService.saveProductDetails(productDetailsDto));
            verify(productDetailsRepository).save(productDetails);
            verify(dtoMapper, never()).entityToDto(any());
        }*/
}