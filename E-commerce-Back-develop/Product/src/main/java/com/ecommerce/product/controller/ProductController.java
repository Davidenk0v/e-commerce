package com.ecommerce.product.controller;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.product.dto.request.ProductCreationDto;
import com.ecommerce.product.dto.response.ErrorResponseDto;
import com.ecommerce.product.dto.response.ProductDto;
import com.ecommerce.product.entity.ModelState;
import com.ecommerce.product.service.IProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product/product")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Operations related to products")
public class ProductController {

	private final IProductService productService;
	
	@ApiResponses(value = {
        @ApiResponse(
        	responseCode = "201", 
			description="Product created successfully"),
        @ApiResponse(
        	responseCode = "400", 
        	description = "Invalid request body",
            content = {
            	@Content(
            		examples = {@ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"User already has a product registered with title 'Móvil Xiaomi'\",\"details\":\"uri=/api/v1/spec\"}")},
            		mediaType = "application/json",
            		schema = @Schema(implementation = ErrorResponseDto.class))})})
    @Operation(
    	summary = "Create a new product",
    	description = "Title and userId combination must be unique among all products")
	@PostMapping
	public ResponseEntity<ProductDto> saveProduct(@RequestBody ProductCreationDto productCreationDto) {
		return productService.saveProduct(productCreationDto);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Product found"),
		@ApiResponse(
			responseCode = "404", 
			description = "Product not found",
			content = {
				@Content(schema = @Schema(implementation = Void.class))})})
	@Operation(
		summary = "Get a product by id", 
		description = "Returns the product with the given id")
	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
		return productService.getProduct(id);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Products found", 
			content = {@Content(examples = {
				@ExampleObject(value = "[{\"id\":1,\"title\":\"Macbook Pro 2021\",\"manufacturer\":\"Apple\",\"sellerId\":\"569g65de-35y6-4552-ac54-e52085109818\",\"categoryIds\":[1],\"modelIds\":[1,10],\"imageUrl\":\"1657783580839-portatil.png\",\"price\":2000.0,\"description\":\"Portátil de última generación en negro azabache, estética lujosa y alta capacidad con 32GB de RAM y memoria SSD de 1TB.\",\"rating\":4.0,\"opinionCount\":3},{\"id\":2,\"title\":\"Dell XPS 13\",\"manufacturer\":\"Dell\",\"sellerId\":\"569g65de-35y6-4552-ac54-e52085109818\",\"categoryIds\":[1],\"modelIds\":[2,11],\"imageUrl\":null,\"price\":1500.0,\"description\":\"Portátil compacto y potente con 16GB de RAM y almacenamiento SSD de 512GB.\",\"rating\":4.0,\"opinionCount\":3}]")})})})
	@Operation(
		summary = "Get all products", 
		description = "Returns all products")
	@GetMapping
	public ResponseEntity<List<ProductDto>> getProducts() {
		return productService.getProducts();
	}
	
	@GetMapping("/search")
	public ResponseEntity<Page<ProductDto>> getFilteredProducts(
		@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "5") Integer size,
        @RequestParam(defaultValue = "[{\"field\":\"title\",\"direction\":\"desc\"}]") String sort,
        @RequestParam(required = false) Set<String> categories,
        @RequestParam(name = "min_price", required = false) Integer minPrice,
        @RequestParam(name = "max_price", required = false) Integer maxPrice,
        @RequestParam(name = "min_rating", required = false) Integer minRating,
		@RequestParam(name = "title", required = false) String title
	) {
		return productService.getFilteredProducts(minPrice, maxPrice, minRating, categories, page, size, sort, title, ModelState.ON_SALE);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "204", 
			description = "Product deleted"),
		@ApiResponse(
			responseCode = "404", 
			description = "Product not found")})
	@Operation(
		summary = "Delete a product by id", 
		description = "Deletes the product with the given id")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		return productService.deleteProduct(id);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Product updated"),
		@ApiResponse(
			responseCode = "404", 
			description = "Product not found",
			content = {
				@Content(schema = @Schema(implementation = Void.class))}),
		@ApiResponse(
        	responseCode = "400", 
        	description = "Invalid request body",
            content = {
            	@Content(
            		examples = {@ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"Spec with name 'color' and value 'rojo' already exists\",\"details\":\"uri=/api/v1/spec\"}")},
            		mediaType = "application/json",
            		schema = @Schema(implementation = ErrorResponseDto.class))})})
	@Operation(
		summary = "Updates a product by id", 
		description = "Updates the product with the given id, checking first if the new combination of sellerId and title is available")
	@PutMapping("/{id}")
	public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductCreationDto productCreationDto) {
		return productService.updateProduct(id, productCreationDto);
	}

	@GetMapping("/search-title/{title}")
	public ResponseEntity<List<ProductDto>> findByTitleContaining(@PathVariable String title) {
		return productService.getProductsByTitleContaining(title);
	}
	
	@GetMapping("/of-seller/{sellerId}")
	public ResponseEntity<List<ProductDto>> getProductsBySellerId(@PathVariable String sellerId) {
		return productService.getProductsBySellerId(sellerId);
	}
}
