package com.ecommerce.product.dto.request;

import java.math.BigDecimal;
import java.util.Set;

import lombok.Builder;

@Builder
public record ModelCreationDto(
	String description, 
	BigDecimal price,
	Long productId,
	Set<Long> specIds
) {}
