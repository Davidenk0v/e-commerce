package com.ecommerce.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.product.entity.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    Optional<Rating> findById(Long id);

	Optional<Rating> findByModelIdAndUserId(Long modelId, String userId);

	List<Rating> findByModelId(Long modelId);

	List<Rating> findAllByUserId(String userId);

	@Query("SELECT r FROM Rating r WHERE r.model.product.id = :productId")
	List<Rating> findAllByProductId(@Param("productId") Long productId);

	@Query("SELECT r FROM Rating r WHERE r.model.product.id = :productId AND r.commentary IS NOT NULL")
	List<Rating> findAllByProductIdWithCommentary(@Param("productId") Long productId);
}
