package com.ecommerce.product.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.ecommerce.product.dto.request.ProductCreationDto;
import com.ecommerce.product.dto.response.ProductDto;
import com.ecommerce.product.entity.ModelState;

public interface IProductService {

	ResponseEntity<ProductDto> saveProduct(ProductCreationDto productCreationDto);
	
	ResponseEntity<ProductDto> getProduct(Long id);

	ResponseEntity<List<ProductDto>> getProducts();

	ResponseEntity<Void> deleteProduct(Long id);

	ResponseEntity<ProductDto> updateProduct(Long id, ProductCreationDto productCreationDto);

	ResponseEntity<Page<ProductDto>> getFilteredProducts(
			Integer minPrice, 
			Integer maxPrice, 
			Integer minRating, 
			Set<String> category, 
			Integer page, 
			Integer size, 
			String sort, 
			String title,
			ModelState modelState);

    ResponseEntity<List<ProductDto>> getProductsByTitleContaining(String title);

	ResponseEntity<List<ProductDto>> getProductsBySellerId(String sellerId);
}
