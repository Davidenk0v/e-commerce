package com.ecommerce.product.mapper.impl;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ecommerce.product.clients.UserClient;
import com.ecommerce.product.dto.request.CommentaryCreationDto;
import com.ecommerce.product.dto.request.UserDto;
import com.ecommerce.product.dto.response.CommentaryDto;
import com.ecommerce.product.entity.Commentary;
import com.ecommerce.product.entity.Image;
import com.ecommerce.product.entity.Model;
import com.ecommerce.product.mapper.IDtoMapper;
import com.ecommerce.product.repository.CommentaryRepository;
import com.ecommerce.product.repository.ModelRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentaryMapper implements IDtoMapper<CommentaryCreationDto, Commentary, CommentaryDto> {

    private final ModelRepository modelRepository;
    private final CommentaryRepository commentaryRepository;
    private final UserClient userClient;

    private static final String TIME_PATTERN = "dd/MM/yyyy";

    @Override
    public Commentary toEntity(CommentaryCreationDto dto) {
        Optional<Model> optModel = modelRepository.findById(dto.modelId());
        if (optModel.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Model with id '%s' does not exist", dto.modelId()));
        }
        Commentary parentCommentary = null;
        if(dto.parentCommentaryId().isPresent()) {
        	Long parentCommentaryId = dto.parentCommentaryId().get();
            Optional<Commentary> optCommentary = commentaryRepository.findById(parentCommentaryId);
            if(optCommentary.isEmpty()) {
                throw new IllegalArgumentException(
                        String.format("Commentary with id '%s' does not exist", parentCommentaryId));
            }
            parentCommentary = optCommentary.get();
        }

        
        return Commentary.builder()
                .text(dto.text())
                .userId(dto.userId())
                .model(optModel.get())
                .images(new ArrayList<>())
                .likes(new ArrayList<>())
                .parentCommentary(parentCommentary)
                .build();
    }

    @Override
    public CommentaryDto toDto(Commentary entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN).withZone(ZoneId.systemDefault());
        
        Long parentCommentaryId = null;
        if(entity.getParentCommentary() != null) {
            parentCommentaryId = entity.getParentCommentary().getId();
        }
        
        List<Long> childCommentaryIds = new ArrayList<>();
        List<Commentary> childCommentaries = entity.getChildCommentaries();
        if (childCommentaries != null) {
        	childCommentaryIds = childCommentaries.stream().map(Commentary::getId).toList();
        }
        String username = "Anónimo";
        try{
            UserDto user = userClient.getUserById(entity.getUserId());
            username = user.username();
        }catch (Exception e){
            log.error("No se encontro el usuario con ese id");
            System.err.println("Error fetching user data: " + e.getMessage());
        }
        
        return CommentaryDto.builder()
                .id(entity.getId())
                .username(username)
                .creationDate(formatter.format(entity.getCreationDate()))
				.images(entity.getImages().stream().map(Image::getUrl).toList())
                .likes(entity.getLikes().size())
                .userId(entity.getUserId())
                .modelId(entity.getModel().getId())
                .text(entity.getText())
                .parentCommentaryId(parentCommentaryId)
                .childCommentaryIds(childCommentaryIds)
                .build();
    }
    
    
}
