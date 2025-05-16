package com.ecommerce.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record LikeCreationDto(
	@Schema(example="569f67de-36e6-4552-ac54-e52085109818")
	String userId,
	@Schema(example="1")
	Long commentaryId
) {}
