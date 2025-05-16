package com.ecommerce.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.product.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByTitle(String title);
	
	List<Category> findAll();
	
	Optional<Category> findById(Long id);
}
