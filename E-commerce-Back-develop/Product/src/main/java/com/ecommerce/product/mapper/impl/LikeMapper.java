package com.ecommerce.product.mapper.impl;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ecommerce.product.dto.request.LikeCreationDto;
import com.ecommerce.product.dto.response.LikeDto;
import com.ecommerce.product.entity.Commentary;
import com.ecommerce.product.entity.Like;
import com.ecommerce.product.mapper.IDtoMapper;
import com.ecommerce.product.repository.CommentaryRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class LikeMapper implements IDtoMapper<LikeCreationDto, Like, LikeDto> {

	private final CommentaryRepository commentaryRepository;
	private static final String TIME_PATTERN = "dd/MM/yyyy";
	
	@Override
	public Like toEntity(LikeCreationDto dto) {
		Optional<Commentary> optCommentary = commentaryRepository.findById(dto.commentaryId());
		if (optCommentary.isEmpty()) {
			throw new IllegalArgumentException(
					String.format("Commentary with id '%s' does not exist", dto.commentaryId()));
		}
		
		return Like.builder()
				.userId(dto.userId())
				.commentary(optCommentary.get())
				.build();
	}

	@Override
	public LikeDto toDto(Like entity) {
		// Should decide on the time zone to use
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN).withZone(ZoneId.systemDefault());
		
		return LikeDto.builder()
				.id(entity.getId())
				.userId(entity.getUserId())
				.commentaryId(entity.getCommentary().getId())
				.creationDate(formatter.format(entity.getCreationDate()))
				.build();
	}
}
