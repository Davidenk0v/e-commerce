package com.ecommerce.cart.mapper.impl;

import com.ecommerce.cart.dto.response.CartDto;
import com.ecommerce.cart.entities.Cart;
import com.ecommerce.cart.mapper.IDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper implements IDtoMapper<Cart, CartDto> {

    @Autowired
    private ProductDetailsMapper productMapper;

    @Override
    public Cart dtoToEntity(CartDto dto) {
        return Cart.builder()
                .id(dto.id())
                .userId(dto.userId())
                .productsDetails(null)
                .build();
    }

    @Override
    public CartDto entityToDto(Cart entity) {

        return CartDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .productsDetails(productMapper.entityListToResponseDtoList(entity.getProductsDetails()))
                .build();
    }

    @Override
    public List<CartDto> entityListToResponseDtoList(List<Cart> entityList) {
        return entityList.stream().map(this::entityToDto).toList();
    }
}
