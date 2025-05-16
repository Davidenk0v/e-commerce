package com.ecommerce.product.dto.response;

import lombok.Builder;

@Builder
public record ImageDto(
	Long id, 
	String url
) {}
