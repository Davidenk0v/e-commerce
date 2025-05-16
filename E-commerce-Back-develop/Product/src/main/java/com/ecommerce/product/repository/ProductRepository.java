package com.ecommerce.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ecommerce.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
	Optional<Product> findById(Long id);
	Optional<Product> findByTitle(String title);
	Optional<Product> findByTitleAndSellerId(String title, String sellerId);
	Page<Product> findAll(Specification<Product> specification, Pageable pagable);
	List<Product> findByTitleContainingIgnoreCase(String title);
	List<Product> findBySellerId(String sellerId);
}
