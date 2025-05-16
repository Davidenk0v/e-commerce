package com.ecommerce.product.repository;

import java.util.List;
import java.util.Optional;

import com.ecommerce.product.entity.Commentary;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.product.entity.Like;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
	
	Optional<Like> findById(Long id);
	
	List<Like> findAllByUserId(String userId);

	List<Like> findAllByCommentary(Commentary commentary);
}
