package com.ecommerce.product.query.dto;

import lombok.Builder;

@Builder
public record ProductSortDto(
	String field,
	String direction
) {}
