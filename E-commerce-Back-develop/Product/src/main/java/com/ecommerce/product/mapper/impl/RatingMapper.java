package com.ecommerce.product.mapper.impl;

import java.util.List;
import java.util.Optional;

import com.ecommerce.product.clients.UserClient;
import com.ecommerce.product.dto.request.RatingCreationDto;
import com.ecommerce.product.dto.request.UserDto;
import com.ecommerce.product.dto.response.CommentaryDto;
import com.ecommerce.product.dto.response.RatingDto;
import com.ecommerce.product.dto.response.SpecDto;
import com.ecommerce.product.entity.Commentary;
import com.ecommerce.product.entity.Model;
import com.ecommerce.product.entity.Rating;
import com.ecommerce.product.mapper.IDtoMapper;
import com.ecommerce.product.repository.CommentaryRepository;
import com.ecommerce.product.repository.ModelRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RatingMapper implements IDtoMapper<RatingCreationDto, Rating, RatingDto> {

	private final ModelRepository modelRepository;

	private final CommentaryMapper commentaryMapper;

	private final SpecMapper specMapper;

	private final UserClient userClient;
	
	@Override
	public Rating toEntity(RatingCreationDto dto) {
		Optional<Model> optModel = modelRepository.findById(dto.modelId());
		if (optModel.isEmpty()) {
			throw new IllegalArgumentException(
	            String.format("Model with id '%s' does not exist", dto.modelId()));
		}
		
		// Commentary is added from the service once it has been successfully saved to the DB
		return Rating.builder()
				.userId(dto.userId())
				.model(optModel.get())
				.value(dto.value())
				.build();
	}

	@Override
	public RatingDto toDto(Rating entity) {
        Commentary commentary = entity.getCommentary();
        CommentaryDto commentaryDto = null;
		List<SpecDto> specs = entity.getModel().getSpecs().stream().map(specMapper::toDto).toList();
        if (commentary != null) {
            commentaryDto = commentaryMapper.toDto(commentary);
        }
        String username = "Anónimo";
        try {
            UserDto userDto = userClient.getUserById(entity.getUserId());
            if (userDto != null) {
                username = userDto.username();
            }
        } catch (Exception e) {
            log.error("Error fetching user data: {}", e.getMessage()); // Cambiarlo por un throw cuando tengamos una lista de usarios reales
        }

        return RatingDto.builder()
                .id(entity.getId())
                .username(username)
                .userId(entity.getUserId())
                .modelId(entity.getModel().getId())
				.specs(specs)
                .value(entity.getValue())
                .commentary(commentaryDto)
                .build();
    }
}
