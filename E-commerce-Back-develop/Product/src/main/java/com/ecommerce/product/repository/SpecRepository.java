package com.ecommerce.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.product.entity.Spec;

@Repository
public interface SpecRepository extends JpaRepository<Spec, Long> {

	Optional<Spec> findByName(String name);
	
	List<Spec> findAll();
	
	List<Spec> findAllByName(String name);
	
	Optional<Spec> findById(Long id);

	Optional<Spec> findByNameAndValue(String name, String value);
}
