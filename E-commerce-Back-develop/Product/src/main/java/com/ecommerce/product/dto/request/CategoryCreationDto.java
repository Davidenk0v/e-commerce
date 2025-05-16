package com.ecommerce.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryCreationDto(
	@Schema(example = "portátiles")
	String title
) {}
