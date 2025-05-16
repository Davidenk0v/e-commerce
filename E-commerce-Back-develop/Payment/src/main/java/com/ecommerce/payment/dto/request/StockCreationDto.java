package com.ecommerce.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record StockCreationDto(
	@Schema(example = "1")
	Long modelId,
	@Schema(example = "10")
	Long quantity,
	@Schema(example = "1")
	Long warehouseId	
) {}
