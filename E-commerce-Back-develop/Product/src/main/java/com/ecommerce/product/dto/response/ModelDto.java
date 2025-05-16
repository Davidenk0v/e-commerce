package com.ecommerce.product.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.ecommerce.product.entity.ModelState;

import lombok.Builder;

@Builder
public record ModelDto(
	Long id,
	Long productId, 
	BigDecimal price,
	List<String> imagesUrl,
	String description,
	ModelState state,
	List<CommentaryDto> commentaries,
	List<SpecDto> specs,
	String productName
) {}

