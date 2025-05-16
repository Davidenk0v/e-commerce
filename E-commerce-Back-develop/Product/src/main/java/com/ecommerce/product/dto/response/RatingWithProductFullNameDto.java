package com.ecommerce.product.dto.response;

import lombok.Builder;

@Builder
public record RatingWithProductFullNameDto(
	RatingDto ratingDto,
	String productFullName
) {}
