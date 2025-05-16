package com.ecommerce.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record StockDto(
	@Schema(example = "1")
	Long id,
	@Schema(example = "1")
	Long modelId,
	@Schema(example = "10")
	Long quantity,
	@Schema(example = "1")
	Long warehouseId
) {}
