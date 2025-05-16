package com.ecommerce.product.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.product.dto.request.CategoryCreationDto;
import com.ecommerce.product.dto.response.CategoryDto;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.mapper.IDtoMapper;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.service.ICategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

	private final CategoryRepository categoryRepository;
	private final IDtoMapper<CategoryCreationDto, Category, CategoryDto> categoryMapper;
	
	@Override
	public ResponseEntity<CategoryDto> saveCategory(CategoryCreationDto categoryCreationDto) {
		Optional<Category> categoryOptional = categoryRepository.findByTitle(categoryCreationDto.title());
		if (categoryOptional.isPresent()) {
            throw new IllegalArgumentException(
            	String.format("Category with title '%s' already exists", categoryCreationDto.title()));
		}
		
		Category category = categoryMapper.toEntity(categoryCreationDto);
		category = categoryRepository.save(category);
		CategoryDto categoryDto = categoryMapper.toDto(category);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(categoryDto);
	}

	@Override
	public ResponseEntity<CategoryDto> getCategory(String title) {
		Optional<Category> categoryOptional = categoryRepository.findByTitle(title);
		if (categoryOptional.isPresent()) {
			CategoryDto categoryDto = categoryMapper.toDto(categoryOptional.get());
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(categoryDto);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@Override
	public ResponseEntity<List<CategoryDto>> getCategories() {
		List<Category> categories = categoryRepository.findAll();
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(categories.stream().map(categoryMapper::toDto).toList());
	}

	@Override
	public ResponseEntity<Void> deleteCategory(String title) {
		Optional<Category> categoryOptional = categoryRepository.findByTitle(title);
		if (categoryOptional.isPresent()) {
			categoryRepository.delete(categoryOptional.get());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@Override
	public ResponseEntity<CategoryDto> updateCategory(Long id, CategoryCreationDto categoryCreationDto) {
		Optional<Category> categoryOptional = categoryRepository.findByTitle(categoryCreationDto.title());
		if (categoryOptional.isPresent()) {
            throw new IllegalArgumentException(
                String.format("Category with title '%s' already exists", categoryCreationDto.title()));
        }
		categoryOptional = categoryRepository.findById(id);
		if (categoryOptional.isPresent()) {
			Category category = categoryOptional.get();
			category.setTitle(categoryCreationDto.title());
			category = categoryRepository.save(category);
			CategoryDto categoryDto = categoryMapper.toDto(category);
			return ResponseEntity
				.status(HttpStatus.OK)
				.body(categoryDto);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
