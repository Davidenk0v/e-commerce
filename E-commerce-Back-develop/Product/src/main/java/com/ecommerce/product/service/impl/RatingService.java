package com.ecommerce.product.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.product.dto.request.CommentaryCreationDto;
import com.ecommerce.product.dto.request.RatingCreationDto;
import com.ecommerce.product.dto.response.CommentaryDto;
import com.ecommerce.product.dto.response.RatingDto;
import com.ecommerce.product.dto.response.RatingWithProductFullNameDto;
import com.ecommerce.product.dto.response.SpecDto;
import com.ecommerce.product.entity.Commentary;
import com.ecommerce.product.entity.Image;
import com.ecommerce.product.entity.Model;
import com.ecommerce.product.entity.Rating;
import com.ecommerce.product.mapper.IDtoMapper;
import com.ecommerce.product.repository.CommentaryRepository;
import com.ecommerce.product.repository.ImageRepository;
import com.ecommerce.product.repository.ModelRepository;
import com.ecommerce.product.repository.RatingRepository;
import com.ecommerce.product.service.IRatingService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingService implements IRatingService {
	
	private final ImageService imageService;
	private final RatingRepository ratingRepository;
	private final ModelRepository modelRepository;
	private final ImageRepository imageRepository;
	private final CommentaryRepository commentaryRepository;
	private final IDtoMapper<RatingCreationDto, Rating, RatingDto> ratingMapper;
	private final IDtoMapper<CommentaryCreationDto, Commentary, CommentaryDto> commentaryMapper;
	
	@Override
	public ResponseEntity<RatingDto> saveRating(RatingCreationDto ratingCreationDto) {
		
		Optional<Rating> optRating = ratingRepository.findByModelIdAndUserId(ratingCreationDto.modelId(), ratingCreationDto.userId());
		if (optRating.isPresent()) {
			throw new IllegalArgumentException(
					String.format("Rating with modelId '%s' and userId '%s' already exists", 
							ratingCreationDto.modelId(),
							ratingCreationDto.userId()));
		}
		
		Optional<Model> optModel = modelRepository.findById(ratingCreationDto.modelId());
		if (optModel.isEmpty()) {
			throw new IllegalArgumentException(
					String.format("Model with id '%s' not found", ratingCreationDto.modelId()));
		}
		
		Commentary commentary = null;
		
		if (ratingCreationDto.optCommentaryCreationDto().isPresent()) {
			CommentaryCreationDto commentaryCreationDto = ratingCreationDto.optCommentaryCreationDto().get();
			commentary = commentaryMapper.toEntity(commentaryCreationDto);
			commentary = commentaryRepository.save(commentary);
		}
		
		Rating rating = ratingMapper.toEntity(ratingCreationDto);
		rating.setCommentary(commentary);
		rating = ratingRepository.save(rating);	
		RatingDto  ratingDto = ratingMapper.toDto(rating);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(ratingDto);
	}

	@Override
	public ResponseEntity<List<RatingDto>> getRatingsByModelId(Long modelId) {
		List<Rating> modelRatings = ratingRepository.findByModelId(modelId);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(modelRatings.stream().map(ratingMapper::toDto).toList());
	}

	@Override
	public ResponseEntity<Void> deleteRating(Long id) {
		Optional<Rating> optRating = ratingRepository.findById(id);
		if(optRating.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		ratingRepository.delete(optRating.get());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Override
	@Transactional
	public ResponseEntity<RatingDto> updateRating(Long id, RatingDto ratingDto) {
		Optional<Rating> optRating = ratingRepository.findById(id);
		if(optRating.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		Rating rating = optRating.get();
		
		// Update value
		rating.setValue(ratingDto.value());
		
		// Update commentary text
		Commentary commentary = rating.getCommentary();
		commentary.setText(ratingDto.commentary().text());
		
		// Update state of images related to the rating, new images are added from the image service when uploaded
		List<Image> previousImages = rating.getCommentary().getImages();
		List<String> newImageUrls = ratingDto.commentary().images();
		List<Image> imagesToDelete = previousImages.stream().filter(img -> !newImageUrls.contains(img.getUrl())).toList();
		for (Image image : imagesToDelete) {
			commentary.getImages().remove(image);
			imageService.deleteImage(image.getUrl());
			imageRepository.delete(image);
		}
		
		// Save commentary and rating
		commentaryRepository.save(commentary);
		ratingRepository.save(rating);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ratingMapper.toDto(rating));
	}


	@Override
	public ResponseEntity<List<RatingDto>> getRatingsWithComentaryByProductId(Long productId) {
		List<Rating> ratings = ratingRepository.findAllByProductIdWithCommentary(productId);

		List<RatingDto> ratingDtos = ratings.stream()
				.map(ratingMapper::toDto)
				.toList();

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ratingDtos);

	}

	@Override
	public ResponseEntity<List<RatingWithProductFullNameDto>> getRatingsByUser(String userId) {
		List<Rating> ratings = ratingRepository.findAllByUserId(userId);

		List<RatingDto> ratingDtos = ratings.stream()
				.map(ratingMapper::toDto)
				.toList();

		List<RatingWithProductFullNameDto> ratingWithProductFullNameDtos = ratingDtos.stream().map(ratingDto -> {
			Optional<Model> optModel = modelRepository.findById(ratingDto.modelId());
			if (optModel.isEmpty()) {
				throw new IllegalArgumentException("No se encontró el modelo asociado al rating");
			}
			Model model = optModel.get();
			
			StringBuilder productFullNameBuilder = new StringBuilder(model.getProduct().getTitle());

			for (SpecDto spec : ratingDto.specs()) {
			    productFullNameBuilder.append(" ").append(spec.value());
			}

			String productFullName = productFullNameBuilder.toString();

			return RatingWithProductFullNameDto.builder()
					.ratingDto(ratingDto)
					.productFullName(productFullName)
					.build();
		}).toList();
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ratingWithProductFullNameDtos);

	}

	@Override
	public ResponseEntity<List<RatingDto>> getAllRatingByProductId(Long productId) {
		List<Rating> ratings = ratingRepository.findAllByProductId(productId);

		List<RatingDto> ratingDtos = ratings.stream()
				.map(ratingMapper::toDto)
				.toList();

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ratingDtos);

	}

	@Override
	public ResponseEntity<RatingDto> getRatingById(Long id) {
		Optional<Rating> rating = ratingRepository.findById(id);

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ratingMapper.toDto(rating.get()));

	}

}
