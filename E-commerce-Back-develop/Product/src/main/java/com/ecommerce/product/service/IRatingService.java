package com.ecommerce.product.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ecommerce.product.dto.request.RatingCreationDto;
import com.ecommerce.product.dto.response.RatingDto;
import com.ecommerce.product.dto.response.RatingWithProductFullNameDto;

public interface IRatingService {

	ResponseEntity<RatingDto> saveRating(RatingCreationDto ratingCreationDto);
	
	ResponseEntity<List<RatingDto>> getRatingsByModelId(Long modelId);
	
	ResponseEntity<Void> deleteRating(Long id);
	
	ResponseEntity<RatingDto> updateRating(Long id, RatingDto ratingDto);

    ResponseEntity<List<RatingDto>> getRatingsWithComentaryByProductId(Long productId);

    ResponseEntity<List<RatingWithProductFullNameDto>> getRatingsByUser(String userId);

    ResponseEntity<List<RatingDto>> getAllRatingByProductId(Long productId);

    ResponseEntity<RatingDto> getRatingById(Long id);
}
