package com.ecommerce.product.dto.request;

import java.util.Optional;

import lombok.Builder;

@Builder
public record RatingCreationDto(
	Long modelId,
	String userId,
	Integer value,
	Optional<CommentaryCreationDto> optCommentaryCreationDto
) {}
