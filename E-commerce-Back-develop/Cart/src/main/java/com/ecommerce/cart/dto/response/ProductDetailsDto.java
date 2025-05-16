package com.ecommerce.cart.dto.response;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record ProductDetailsDto(
        Long id,
        Long cartId,
        Long modelId,
        BigDecimal price,
        Integer quantity
) {
}
