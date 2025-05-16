package com.ecommerce.cart.dto.response;

import lombok.Builder;

@Builder
public record WantedDto(
        Long id,
        String userId,
        Long modelId
) {
}
