package com.ecommerce.product.dto.response;

import lombok.Builder;

@Builder
public record ErrorResponseDto(
	String status,
	String message, 
	String details
) {}
