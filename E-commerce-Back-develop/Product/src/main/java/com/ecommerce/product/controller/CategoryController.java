package com.ecommerce.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.product.dto.request.CategoryCreationDto;
import com.ecommerce.product.dto.response.CategoryDto;
import com.ecommerce.product.dto.response.ErrorResponseDto;
import com.ecommerce.product.service.ICategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product/category")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Operations related to categories")
public class CategoryController {

	private final ICategoryService categoryService;
	
	@ApiResponses(value = {
        @ApiResponse(
        	responseCode = "201", 
			description="Content created successfully"),
        @ApiResponse(
        	responseCode = "400", 
        	description = "Invalid request body",
            content = {
            	@Content(
            		examples = {@ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"Category with title 'portátiles' already exists\",\"details\":\"uri=/api/v1/category\"}")},
            		mediaType = "application/json",
            		schema = @Schema(implementation = ErrorResponseDto.class))})})
    @Operation(
    	summary = "Create a new category",
    	description = "Title must be unique among all categories")
	@PostMapping
	public ResponseEntity<CategoryDto> saveCategory(@RequestBody CategoryCreationDto categoryCreationDto) {
		return categoryService.saveCategory(categoryCreationDto);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Category found"),
		@ApiResponse(
			responseCode = "404", 
			description = "Category not found",
			content = {
				@Content(schema = @Schema(implementation = Void.class))})})
	@Operation(
		summary = "Get a category by title", 
		description = "Returns the category with the given title")
	@GetMapping("/{title}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable String title) {
		return categoryService.getCategory(title);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Categories found", 
			content = {@Content(examples = {
				@ExampleObject(value = "[{\"id\":1,\"title\":\"portátiles\"},{\"id\":2,\"title\":\"smartphones\"}]")})})})
	@Operation(
		summary = "Get all categories", 
		description = "Returns all categories")
	@GetMapping
	public ResponseEntity<List<CategoryDto>> getCategories() {
		return categoryService.getCategories();
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "204", 
			description = "Category deleted"),
		@ApiResponse(
			responseCode = "404", 
			description = "Category not found")})
	@Operation(
		summary = "Delete a category by title", 
		description = "Deletes the category with the given title")
	@DeleteMapping("/{title}")
	public ResponseEntity<Void> deleteCategory(@PathVariable String title) {
		return categoryService.deleteCategory(title);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Category updated"),
		@ApiResponse(
			responseCode = "404", 
			description = "Category not found",
			content = {
				@Content(schema = @Schema(implementation = Void.class))}),
		@ApiResponse(
        	responseCode = "400", 
        	description = "Invalid request body",
            content = {
            	@Content(
            		examples = {@ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"Category with title 'portátiles' already exists\",\"details\":\"uri=/api/v1/category\"}")},
            		mediaType = "application/json",
            		schema = @Schema(implementation = ErrorResponseDto.class))})})
	@Operation(
			summary = "Updates a category by id", 
			description = "Updates the category with the given id, checking first if the new title is available")
	@PutMapping("/{id}")
	public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryCreationDto categoryCreationDto) {
		return categoryService.updateCategory(id, categoryCreationDto);
	}
}
