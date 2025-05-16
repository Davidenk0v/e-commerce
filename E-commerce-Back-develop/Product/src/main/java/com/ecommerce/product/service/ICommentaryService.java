package com.ecommerce.product.service;

import org.springframework.http.ResponseEntity;

import com.ecommerce.product.dto.request.CommentaryCreationDto;
import com.ecommerce.product.dto.response.CommentaryDto;

import java.util.List;

public interface ICommentaryService {

	ResponseEntity<CommentaryDto> saveCommentary(CommentaryCreationDto commentaryCreationDto);
	
	ResponseEntity<CommentaryDto> getCommentary(Long commentaryId);
	
	ResponseEntity<Void> deleteCommentary(Long id);

	ResponseEntity<CommentaryDto> updateCommentary(Long id, CommentaryCreationDto commentaryCreationDto);

	ResponseEntity<List<CommentaryDto>> getAllCommentariesByParentId(Long id);

	ResponseEntity<List<CommentaryDto>> getCommentariesByModelProductIdAndRatingNullAndParentCommentaryNull(Long productId);

	ResponseEntity<List<CommentaryDto>> getCommentariesByUserIdAndRatingNullAndParentCommentaryNull(String userId);
}
