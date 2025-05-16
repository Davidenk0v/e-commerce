package com.ecommerce.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record SpecDto(
	@Schema(example = "1")
	Long id,
	
	@Schema(example = "color")
	String name, 
	
	@Schema(example = "rojo")
	String value
) {}
