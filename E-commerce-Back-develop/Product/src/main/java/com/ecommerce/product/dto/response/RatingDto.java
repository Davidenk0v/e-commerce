package com.ecommerce.product.dto.response;

import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
public record RatingDto(
	Long id, 
	Long modelId, 
	String userId,
	String username,
	@Min(1)
	@Max(5)
	Integer value,
	List<SpecDto> specs,
	@Nullable
	CommentaryDto commentary
) {}
