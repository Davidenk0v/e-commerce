package com.ecommerce.product.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record CommentaryDto(
	Long id,
	String username,
	String userId, 
	Long modelId, 
	String text,
	String creationDate,
	List<String> images,
	Integer likes,
	Long parentCommentaryId,
	List<Long> childCommentaryIds
) {}
