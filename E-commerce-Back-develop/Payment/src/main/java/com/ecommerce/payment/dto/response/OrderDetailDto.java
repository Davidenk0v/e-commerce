package com.ecommerce.payment.dto.response;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record OrderDetailDto(
	Long id,
	Long modelId,
	String name,
	BigDecimal price,
	Long quantity,
	Long orderId
) {}
