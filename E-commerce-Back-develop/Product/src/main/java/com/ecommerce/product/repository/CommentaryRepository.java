package com.ecommerce.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.product.entity.Commentary;

@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, Long> {

	List<Commentary> findByUserId(String string);

	Optional<Commentary> findByParentCommentaryIdAndUserId(Long referencedCommentaryId, String userId);

	List<Commentary> findByParentCommentaryId(Long referencedCommentaryId);

	Optional<Commentary> findByUserIdAndModelIdAndParentCommentaryIsNullAndRatingIsNotNull(String userId, Long modelId);

	List<Commentary> findByModelProductIdAndRatingIsNullAndParentCommentaryIsNull(Long productId);

	List<Commentary> findByUserIdAndRatingIsNullAndParentCommentaryIsNull(String userId);
}
