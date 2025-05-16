package com.ecommerce.payment.query.dto;

import lombok.Builder;

@Builder
public record SortDto(
	String field,
	String direction
) {}
