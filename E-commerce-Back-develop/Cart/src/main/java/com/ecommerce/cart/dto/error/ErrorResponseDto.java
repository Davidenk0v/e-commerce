package com.ecommerce.cart.dto.error;

import lombok.Builder;

@Builder
public record ErrorResponseDto(
        String status,
        String message,
        String details
) {}
