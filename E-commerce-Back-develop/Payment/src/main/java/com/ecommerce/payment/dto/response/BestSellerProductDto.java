package com.ecommerce.payment.dto.response;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record BestSellerProductDto(
	String name,
	Integer modelId,
	Integer quantitySold,
	BigDecimal totalSales
) {}
