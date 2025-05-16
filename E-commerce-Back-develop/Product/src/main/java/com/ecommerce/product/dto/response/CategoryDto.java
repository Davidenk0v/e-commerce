package com.ecommerce.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CategoryDto(
	@Schema(example = "1")
	Long id,
	
	@Schema(example = "portátiles")
	String title
) {}
