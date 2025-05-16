package com.ecommerce.payment.dto.request;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record OrderDetailCreationDto ( 
	Long modelId,
	String name,
	BigDecimal price,
	Long quantity
) {}
