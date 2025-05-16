package com.ecommerce.cart.mapper.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ecommerce.cart.dto.response.ProductDetailsDto;
import com.ecommerce.cart.entities.Cart;
import com.ecommerce.cart.entities.ProductDetails;
import com.ecommerce.cart.mapper.IDtoMapper;
import com.ecommerce.cart.repositories.CartRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductDetailsMapper implements IDtoMapper<ProductDetails, ProductDetailsDto> {

    private final CartRepository cartRepository;

    @Override
    public ProductDetails dtoToEntity(ProductDetailsDto dto) {
        Optional<Cart> cart = cartRepository.findById(dto.cartId());
        if(cart.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el carrito del usuario");
        }
        return ProductDetails.builder()
                .id(dto.id())
                .modelId(dto.modelId())
                .quantity(dto.quantity())
                .price(dto.price())
                .cart(cart.get())
                .build();
    }

    @Override
    public ProductDetailsDto entityToDto(ProductDetails entity) {
        return ProductDetailsDto.builder()
                .id(entity.getId())
                .modelId(entity.getModelId())
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .cartId(entity.getCart().getId())
                .build();
    }

    @Override
    public List<ProductDetailsDto> entityListToResponseDtoList(List<ProductDetails> entityList) {
        return entityList.stream().map(this::entityToDto).toList();
    }
}
