package com.ecommerce.product.service;


import com.ecommerce.product.dto.request.LikeCreationDto;
import com.ecommerce.product.dto.response.LikeDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ILikeService {

    ResponseEntity<LikeDto> saveLike(LikeCreationDto likeCreationDto);

    ResponseEntity<LikeDto> getLike(Long id);

    ResponseEntity<List<LikeDto>> getLikes();

    ResponseEntity<List<LikeDto>> getLikesByCommentary(Long comentaryId);

    ResponseEntity<List<LikeDto>> getLikesByUser(String userId);

    ResponseEntity<Void> deleteLike(Long id);
}
