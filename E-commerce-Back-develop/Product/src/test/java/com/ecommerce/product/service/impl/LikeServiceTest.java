package com.ecommerce.product.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ecommerce.product.dto.request.LikeCreationDto;
import com.ecommerce.product.dto.response.LikeDto;
import com.ecommerce.product.entity.Commentary;
import com.ecommerce.product.entity.Like;
import com.ecommerce.product.mapper.IDtoMapper;
import com.ecommerce.product.repository.CommentaryRepository;
import com.ecommerce.product.repository.LikeRepository;

class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private CommentaryRepository commentaryRepository;

    @Mock
    private IDtoMapper<LikeCreationDto, Like, LikeDto> likeMapper;

    @InjectMocks
    private LikeService likeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveLike_ValidInput_ReturnsCreatedLikeDto() {
        // Arrange
        LikeCreationDto likeCreationDto = new LikeCreationDto("user123", 1L);
        Commentary commentary = new Commentary();
        commentary.setId(1L);
        Like like = new Like();
        like.setId(1L);
        like.setCommentary(commentary);
        like.setUserId("user123");
        LikeDto likeDto = new LikeDto(1L, "user123", 1L, null);

        when(commentaryRepository.findById(1L)).thenReturn(Optional.of(commentary));
        when(likeRepository.save(any(Like.class))).thenReturn(like);
        when(likeMapper.toDto(like)).thenReturn(likeDto);

        // Act
        ResponseEntity<LikeDto> response = likeService.saveLike(likeCreationDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(likeDto, response.getBody());
        verify(commentaryRepository).findById(1L);
        verify(likeRepository).save(any(Like.class));
        verify(likeMapper).toDto(like);
    }

    @Test
    void saveLike_CommentaryNotFound_ThrowsIllegalArgumentException() {
        // Arrange
        LikeCreationDto likeCreationDto = new LikeCreationDto("user123", 1L);
        when(commentaryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            likeService.saveLike(likeCreationDto);
        });
        assertEquals("Commentary with id '1' not found", exception.getMessage());
        verify(commentaryRepository).findById(1L);
        verify(likeRepository, never()).save(any(Like.class));
    }

    @Test
    void getLike_ExistingLike_ReturnsLikeDto() {
        // Arrange
        Long commentaryId = 1L;
        Long likeId = 1L;
        Like like = new Like();
        like.setId(likeId);
        LikeDto likeDto = new LikeDto(1L,  "user123",commentaryId, null);

        when(likeRepository.findById(likeId)).thenReturn(Optional.of(like));
        when(likeMapper.toDto(like)).thenReturn(likeDto);

        // Act
        ResponseEntity<LikeDto> response = likeService.getLike(likeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(likeDto, response.getBody());
        verify(likeRepository).findById(likeId);
        verify(likeMapper).toDto(like);
    }

    @Test
    void getLike_NonExistingLike_ThrowsIllegalArgumentException() {
        // Arrange
        Long likeId = 1L;
        when(likeRepository.findById(likeId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            likeService.getLike(likeId);
        });
        assertEquals("Like with id '1' not found", exception.getMessage());
        verify(likeRepository).findById(likeId);
    }

    @Test
    void getLikes_ExistingLikes_ReturnsListOfLikeDtos() {
        // Arrange
        Long commentaryId = 1L;
        Like like1 = new Like();
        like1.setId(1L);
        Like like2 = new Like();
        like2.setId(2L);
        List<Like> likes = Arrays.asList(like1, like2);

        LikeDto likeDto1 = new LikeDto(1L,  "user123",commentaryId, null);
        LikeDto likeDto2 = new LikeDto(2L,  "user456",commentaryId, null);
        List<LikeDto> likeDtos = Arrays.asList(likeDto1, likeDto2);

        when(likeRepository.findAll()).thenReturn(likes);
        when(likeMapper.toDto(like1)).thenReturn(likeDto1);
        when(likeMapper.toDto(like2)).thenReturn(likeDto2);

        // Act
        ResponseEntity<List<LikeDto>> response = likeService.getLikes();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(likeDtos, response.getBody());
        verify(likeRepository).findAll();
        verify(likeMapper, times(2)).toDto(any(Like.class));
    }

    @Test
    void getLikesByCommentary_ExistingCommentaryAndLikes_ReturnsListOfLikeDtos() {
        // Arrange
        Long commentaryId = 1L;
        Commentary commentary = new Commentary();
        commentary.setId(commentaryId);

        Like like1 = new Like();
        like1.setId(1L);
        Like like2 = new Like();
        like2.setId(2L);
        List<Like> likes = Arrays.asList(like1, like2);

        LikeDto likeDto1 = new LikeDto(1L,  "user123",commentaryId, null);
        LikeDto likeDto2 = new LikeDto(2L,  "user456",commentaryId, null);
        List<LikeDto> likeDtos = Arrays.asList(likeDto1, likeDto2);

        when(commentaryRepository.findById(commentaryId)).thenReturn(Optional.of(commentary));
        when(likeRepository.findAllByCommentary(commentary)).thenReturn(likes);
        when(likeMapper.toDto(like1)).thenReturn(likeDto1);
        when(likeMapper.toDto(like2)).thenReturn(likeDto2);

        // Act
        ResponseEntity<List<LikeDto>> response = likeService.getLikesByCommentary(commentaryId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(likeDtos, response.getBody());
        verify(commentaryRepository).findById(commentaryId);
        verify(likeRepository).findAllByCommentary(commentary);
        verify(likeMapper, times(2)).toDto(any(Like.class));
    }

    @Test
    void getLikesByCommentary_NonExistingCommentary_ThrowsIllegalArgumentException() {
        // Arrange
        Long commentaryId = 1L;
        when(commentaryRepository.findById(commentaryId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            likeService.getLikesByCommentary(commentaryId);
        });
        assertEquals("Commentary with id '1' not found", exception.getMessage());
        verify(commentaryRepository).findById(commentaryId);
        verify(likeRepository, never()).findAllByCommentary(any(Commentary.class));
    }

    @Test
    void getLikesByUser_ExistingLikes_ReturnsListOfLikeDtos() {
        // Arrange
        String userId = "user123";
        Like like1 = new Like();
        like1.setId(1L);
        Like like2 = new Like();
        like2.setId(2L);
        List<Like> likes = Arrays.asList(like1, like2);

        LikeDto likeDto1 = new LikeDto(null,  userId, 4L, null);
        LikeDto likeDto2 = new LikeDto(null,  userId, 5L, null);
        List<LikeDto> likeDtos = Arrays.asList(likeDto1, likeDto2);

        when(likeRepository.findAllByUserId(userId)).thenReturn(likes);
        when(likeMapper.toDto(like1)).thenReturn(likeDto1);
        when(likeMapper.toDto(like2)).thenReturn(likeDto2);

        // Act
        ResponseEntity<List<LikeDto>> response = likeService.getLikesByUser(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(likeDtos, response.getBody());
        verify(likeRepository).findAllByUserId(userId);
        verify(likeMapper, times(2)).toDto(any(Like.class));
    }

    @Test
    void deleteLike_ExistingLike_ReturnsSuccessMessage() {
        // Arrange
        Long likeId = 1L;
        Like like = new Like();
        like.setId(likeId);

        when(likeRepository.findById(likeId)).thenReturn(Optional.of(like));

        // Act
        ResponseEntity<Void> response = likeService.deleteLike(likeId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(likeRepository).findById(likeId);
        verify(likeRepository).delete(like);
    }
}
