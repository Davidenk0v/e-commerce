package com.ecommerce.cart.dto.request;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ModelDto(
	Long id,
	Long productId, 
	BigDecimal price,
	List<String> imagesUrl,
	String description,
	ModelState state,
	List<CommentaryDto> commentaries,
	List<SpecDto> specs
) {}

