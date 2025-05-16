package com.ecommerce.gateway.dtos.error;

import lombok.Builder;

@Builder
public record ErrorResponseDto(
        String status,
        String message,
        String details
) {}
