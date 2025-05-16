package com.ecommerce.product.service.impl;

import com.ecommerce.product.dto.request.LikeCreationDto;
import com.ecommerce.product.dto.response.LikeDto;
import com.ecommerce.product.entity.Commentary;
import com.ecommerce.product.entity.Like;
import com.ecommerce.product.mapper.IDtoMapper;
import com.ecommerce.product.repository.CommentaryRepository;
import com.ecommerce.product.repository.LikeRepository;
import com.ecommerce.product.service.ILikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService implements ILikeService {

    private final LikeRepository likeRepository;
    private final CommentaryRepository commentaryRepository;
    private final IDtoMapper<LikeCreationDto, Like, LikeDto> likeMapper;

    @Override
    public ResponseEntity<LikeDto> saveLike(LikeCreationDto likeCreationDto) {
        Optional<Commentary> optCommentary = commentaryRepository.findById(likeCreationDto.commentaryId());
        if(optCommentary.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Commentary with id '%s' not found", likeCreationDto.commentaryId()));
        }
        Commentary commentary = optCommentary.get();
        Like like = Like.builder()
                .commentary(commentary)
                .userId(likeCreationDto.userId())
                .build();
        like = likeRepository.save(like);
        return ResponseEntity.status(HttpStatus.CREATED).body(likeMapper.toDto(like));
    }

    @Override
    public ResponseEntity<LikeDto> getLike(Long id) {
        Optional<Like> optLike = likeRepository.findById(id);
        if(optLike.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Like with id '%s' not found", id));
        }
        return ResponseEntity.ok(likeMapper.toDto(optLike.get()));
    }

    @Override
    public ResponseEntity<List<LikeDto>> getLikes() {
        List<Like> likes = likeRepository.findAll();
        List<LikeDto> likeDtos = likes.stream().map(likeMapper::toDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(likeDtos);
    }

    @Override
    public ResponseEntity<List<LikeDto>> getLikesByCommentary(Long comentaryId) {
        Optional<Commentary> optCommentary = commentaryRepository.findById(comentaryId);
        if(optCommentary.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Commentary with id '%s' not found", comentaryId));
        }
        List<Like> likes = likeRepository.findAllByCommentary(optCommentary.get());
        List<LikeDto> likeDtos = likes.stream().map(likeMapper::toDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(likeDtos);
    }

    @Override
    public ResponseEntity<List<LikeDto>> getLikesByUser(String userId) {
        //Deberíamos controlar si el usuario existe aquí
        List<Like> likes = likeRepository.findAllByUserId(userId);
        List<LikeDto> likeDtos = likes.stream().map(likeMapper::toDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(likeDtos);
    }

    @Override
    public ResponseEntity<Void> deleteLike(Long id) {
        Optional<Like> optLike = likeRepository.findById(id);
        if(optLike.isEmpty()){
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        likeRepository.delete(optLike.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
