package com.ecommerce.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record SpecCreationDto(
	@Schema(example = "color")
	String name,
	
	@Schema(example = "rojo")
	String value
) {}
