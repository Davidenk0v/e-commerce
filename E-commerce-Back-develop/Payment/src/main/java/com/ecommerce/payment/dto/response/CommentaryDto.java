package com.ecommerce.payment.dto.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record CommentaryDto(
	Long id, 
	String userId, 
	Long modelId, 
	String text,
	String creationDate,
	Set<String> images,
	Integer likes,
	Long referencedCommentaryId
) {}
