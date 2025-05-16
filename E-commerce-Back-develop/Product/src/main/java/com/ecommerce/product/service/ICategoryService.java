package com.ecommerce.product.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ecommerce.product.dto.request.CategoryCreationDto;
import com.ecommerce.product.dto.response.CategoryDto;

public interface ICategoryService {

	ResponseEntity<CategoryDto> saveCategory(CategoryCreationDto categoryCreationDto);
	
	ResponseEntity<CategoryDto> getCategory(String title);
	
	ResponseEntity<List<CategoryDto>> getCategories();
	
	ResponseEntity<Void> deleteCategory(String title);
	
	ResponseEntity<CategoryDto> updateCategory(Long id, CategoryCreationDto categoryCreationDto);
}
