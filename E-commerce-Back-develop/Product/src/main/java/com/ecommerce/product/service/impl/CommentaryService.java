package com.ecommerce.product.service.impl;

import java.util.List;
import java.util.Optional;

import com.ecommerce.product.dto.response.MessageEvent;
import com.ecommerce.product.entity.MessageType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.product.dto.request.CommentaryCreationDto;
import com.ecommerce.product.dto.response.CommentaryDto;
import com.ecommerce.product.entity.Commentary;
import com.ecommerce.product.entity.Model;
import com.ecommerce.product.mapper.IDtoMapper;
import com.ecommerce.product.repository.CommentaryRepository;
import com.ecommerce.product.repository.ModelRepository;
import com.ecommerce.product.service.ICommentaryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentaryService implements ICommentaryService {

	private final CommentaryRepository commentaryRepository;
	private final ModelRepository modelRepository;
	private final IDtoMapper<CommentaryCreationDto, Commentary, CommentaryDto> commentaryMapper;
	private final KafkaService kafkaService;

	static final String MESSAGE_RESPONSE = "Han respondido a tu comentario";
	
	@Override
	public ResponseEntity<CommentaryDto> saveCommentary(CommentaryCreationDto commentaryCreationDto) {
		MessageEvent messageEvent = null;
		
		// Check that the related model exists and gets it
		Optional<Model> optModel = modelRepository.findById(commentaryCreationDto.modelId());
		if (optModel.isEmpty()) {
			throw new IllegalArgumentException(
					String.format("Model with id '%s' does not exist", commentaryCreationDto.modelId()));
		}
		
		Optional<Long> parentCommentaryId = commentaryCreationDto.parentCommentaryId();
		
		if (parentCommentaryId.isPresent()) {
			Long referencedCommentaryId = parentCommentaryId.get();
			Optional<Commentary> optCommentary = commentaryRepository.findById(referencedCommentaryId);
			if(optCommentary.isPresent()) {
				Commentary commentary = optCommentary.get();
				// Check if the user is trying to comment to its own commentary or question
				Commentary referencedCommentary = optCommentary.get();
				if (referencedCommentary.getUserId().equals(commentaryCreationDto.userId())) {
					throw new IllegalArgumentException(String
							.format("User cannot comment to its own Commentary with id '%s'", referencedCommentaryId));
				}
				messageEvent = new MessageEvent(commentary.getUserId(),commentary.getId(), MESSAGE_RESPONSE, String.valueOf(System.currentTimeMillis()), MessageType.QUESTION);
			} else {
				throw new IllegalArgumentException(
						String.format("Commentary with id '%s' does not exist", referencedCommentaryId));
			}
			
			// Check if there is already a commentary for the same commentary by the user
			optCommentary = commentaryRepository.findByParentCommentaryIdAndUserId(referencedCommentaryId, commentaryCreationDto.userId());
			if (optCommentary.isPresent()) {
                throw new IllegalArgumentException(
                	String.format("User has aldready commented on the Commentary with id '%s'", referencedCommentaryId));
            }
			
        }
		
		Commentary commentary = commentaryMapper.toEntity(commentaryCreationDto);
		commentary = commentaryRepository.save(commentary);
		CommentaryDto commentaryDto = commentaryMapper.toDto(commentary);
		
		// Send notification to the user that has commented
		if (messageEvent != null) {
			kafkaService.sendProductMessage(messageEvent);
		}
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(commentaryDto);
	}

	@Override
	public ResponseEntity<CommentaryDto> getCommentary(Long id) {
		Optional<Commentary> commentaryOptional = commentaryRepository.findById(id);
		if (commentaryOptional.isPresent()) {
			CommentaryDto commentaryDto = commentaryMapper.toDto(commentaryOptional.get());
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(commentaryDto);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}


	@Override
	public ResponseEntity<Void> deleteCommentary(Long id) {
		Optional<Commentary> commentaryOptional = commentaryRepository.findById(id);
		if (commentaryOptional.isPresent()) {
			Commentary commentary = commentaryOptional.get();
			commentaryRepository.delete(commentary);
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}


	@Override
	public ResponseEntity<CommentaryDto> updateCommentary(Long id, CommentaryCreationDto commentaryCreationDto) {
		Optional<Commentary> commentaryOptional = commentaryRepository.findById(id);
		if (!commentaryOptional.isPresent() ) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();	
		}
		
		Commentary commentary = commentaryOptional.get();
		commentary.setText(commentaryCreationDto.text());
		commentary = commentaryRepository.save(commentary);
		CommentaryDto commentaryDto = commentaryMapper.toDto(commentary);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(commentaryDto);
	}


	@Override
	public ResponseEntity<List<CommentaryDto>> getAllCommentariesByParentId(Long id) {
		List<Commentary> commentaries = commentaryRepository.findByParentCommentaryId(id);
		if (commentaries.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		List<CommentaryDto> commentariesDto = commentaries.stream()
				.map(commentaryMapper::toDto)
				.toList();

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(commentariesDto);
	}

	@Override
	public ResponseEntity<List<CommentaryDto>> getCommentariesByModelProductIdAndRatingNullAndParentCommentaryNull(Long productId) {
		List<Commentary> commentaries = commentaryRepository.findByModelProductIdAndRatingIsNullAndParentCommentaryIsNull(productId);
		List<CommentaryDto> commentariesDto = commentaries.stream()
				.map(commentaryMapper::toDto)
				.toList();
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(commentariesDto);
	}

	@Override
	public ResponseEntity<List<CommentaryDto>> getCommentariesByUserIdAndRatingNullAndParentCommentaryNull(
			String userId) {
		List<Commentary> commentaries = commentaryRepository.findByUserIdAndRatingIsNullAndParentCommentaryIsNull(userId);
		List<CommentaryDto> commentariesDto = commentaries.stream()
				.map(commentaryMapper::toDto)
				.toList();
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(commentariesDto);
	}
}
