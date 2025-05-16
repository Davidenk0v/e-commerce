package com.ecommerce.product.mapper.impl;

import org.springframework.stereotype.Component;

import com.ecommerce.product.dto.request.CategoryCreationDto;
import com.ecommerce.product.dto.response.CategoryDto;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.mapper.IDtoMapper;

@Component
public class CategoryMapper implements IDtoMapper<CategoryCreationDto, Category, CategoryDto>{

	@Override
	public Category toEntity(CategoryCreationDto dto) {
		return Category.builder()
				.title(dto.title())
				.build();
	}

	@Override
	public CategoryDto toDto(Category entity) {
		return CategoryDto.builder()
				.id(entity.getId())
				.title(entity.getTitle())
				.build();
	}
}
