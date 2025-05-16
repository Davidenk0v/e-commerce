package com.ecommerce.product.dto.request;

import java.util.Optional;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CommentaryCreationDto(
	@Schema(example = "1")
	Long modelId,
	@Schema(example = "Gran relación calidad precio. Muy satisfecho con mi compra.")
	String text,
	@Schema(example = "432u67je-36r6-4532-ac52-e43246489998")
	String userId,
	@Schema(example = "2")
	Optional<Long> parentCommentaryId
) {}
