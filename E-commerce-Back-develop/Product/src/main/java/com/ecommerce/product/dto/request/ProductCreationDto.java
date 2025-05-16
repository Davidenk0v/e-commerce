package com.ecommerce.product.dto.request;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ProductCreationDto(
	@Schema(example = "Iphone 10")
	String title,
	
	@Schema(example="569f67de-36e6-4552-ac54-e52085109818")
	String sellerId,
	
	@Schema(example="Apple")
	String manufacturer,
	
	@Schema(example="[1, 2]")
	Set<Long> categoryIds
) {}
