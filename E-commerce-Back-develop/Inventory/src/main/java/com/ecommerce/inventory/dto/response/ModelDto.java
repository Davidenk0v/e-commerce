package com.ecommerce.inventory.dto.response;

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

