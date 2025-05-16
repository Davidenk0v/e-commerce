package com.ecommerce.product.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.product.dto.request.ProductCreationDto;
import com.ecommerce.product.dto.response.ProductDto;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.ModelState;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.mapper.impl.ProductMapper;
import com.ecommerce.product.query.dto.ProductFilterDto;
import com.ecommerce.product.query.dto.ProductSortDto;
import com.ecommerce.product.query.specification.ProductSpecification;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.IProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
	
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ProductMapper productMapper;
	
	@Override
	public ResponseEntity<ProductDto> saveProduct(ProductCreationDto productCreationDto) {
		Optional<Product> productOptional = productRepository.findByTitleAndSellerId(productCreationDto.title(), productCreationDto.sellerId());
		if (productOptional.isPresent()) {
			throw new IllegalArgumentException(
				String.format("User already has a product registered with title '%s'", productCreationDto.title()));
		}
		
		Product product = productMapper.toEntity(productCreationDto);

		List<Category> categories = new ArrayList<>();
		for (Long categoryId : productCreationDto.categoryIds()) {
			Optional<Category> optCategory = categoryRepository.findById(categoryId);
			if (optCategory.isPresent()) {
                Category category = optCategory.get();
                categories.add(category);
			} else {
				throw new IllegalArgumentException(String.format("Category with id '%s' not found", categoryId));
			}
		}

		product.setCategories(categories);
		product = productRepository.save(product);
		
		ProductDto productDto = productMapper.toDto(product);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(productDto);
	}

	@Override
	public ResponseEntity<ProductDto> getProduct(Long id) {
		Optional<Product> productOptional = productRepository.findById(id);
		if (productOptional.isPresent()) {
			ProductDto productDto = productMapper.toDto(productOptional.get());
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(productDto);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@Override
	public ResponseEntity<List<ProductDto>> getProducts() {
		List<Product> products = productRepository.findAll();
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(products.stream().map(productMapper::toDto).toList());
	}

	@Override
	public ResponseEntity<Void> deleteProduct(Long id) {
		Optional<Product> optProduct = productRepository.findById(id);
		if (optProduct.isPresent()) {
			productRepository.delete(optProduct.get());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@Override
	public ResponseEntity<ProductDto> updateProduct(Long id, ProductCreationDto productCreationDto) {
		// Checks if the product to be updated exists
		Optional<Product> productOptional = productRepository.findById(id);
		if (productOptional.isPresent()) {
			Product product = productOptional.get();
			// Check if the title is available for the seller
			if (!product.getTitle().equals(productCreationDto.title())) {
				productOptional = productRepository.findByTitleAndSellerId(productCreationDto.title(), productCreationDto.sellerId());
				if (productOptional.isPresent()) {
		            throw new IllegalArgumentException(
		                String.format("Product with title '%s' and sellerId '%s' already exists", productCreationDto.title(), productCreationDto.sellerId()));
		        }
			}
			
			// Gets the categories for the product
			List<Category> categories = new ArrayList<>();
			for (Long categoryId : productCreationDto.categoryIds()) {
				Optional<Category> optCategory = categoryRepository.findById(categoryId);
				if (optCategory.isPresent()) {
	                Category category = optCategory.get();
	                categories.add(category);
				} else {
					throw new IllegalArgumentException(String.format("Category with id '%s' not found", categoryId));
				}	
			}
			
			// Set the new values
			product.setTitle(productCreationDto.title());
			product.setManufacturer(productCreationDto.manufacturer());
			product.setCategories(categories);
			product = productRepository.save(product);
			
			// Return the updated product as a DTO
			ProductDto productDto = productMapper.toDto(product);
			return ResponseEntity
				.status(HttpStatus.OK)
				.body(productDto);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@Override
	public ResponseEntity<Page<ProductDto>> getFilteredProducts(
		Integer minPrice, 
		Integer maxPrice, 
		Integer minRating, 
		Set<String> categories, 
		Integer page, 
		Integer size, 
		String sort,
		String title,
		ModelState modelState
	) {
		// Filter DTO
		ProductFilterDto productFilterDto = ProductFilterDto
											.builder()
											.minPrice(minPrice)
											.maxPrice(maxPrice)
											.minRating(minRating)
											.categories(categories)
											.title(title)
											.modelState(modelState)
											.build();
		
		// Parse and create sort orders
        List<ProductSortDto> productSortDtos = jsonStringToSortDto(sort);
        List<Sort.Order> orders = new ArrayList<>();
        
        if (productSortDtos != null) {
            for(ProductSortDto productSortDto: productSortDtos) {
                Sort.Direction direction = 
                	Objects.equals(productSortDto.direction(), "desc") 
                	? Sort.Direction.DESC 
                	: Sort.Direction.ASC;
                orders.add(new Sort.Order(direction, productSortDto.field()));
            }
        }
        
        // Create page request with sorting
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(orders));
        
        // Apply specification and pagination
        Specification<Product> specification = ProductSpecification.getSpecification(productFilterDto);
        Page<Product> products = productRepository.findAll(specification, pageRequest); 
        
        // Map to DTO taking into account if there are filters to be applied to the models
		if (productFilterDto.minPrice() != null || productFilterDto.maxPrice() != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(products.map(product -> productMapper.toDto(product, productFilterDto)));
		}
		
        Page<ProductDto> productDtos = products.map(product -> productMapper.toDto(product, productFilterDto));
  
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(productDtos);
	}
	
	private List<ProductSortDto> jsonStringToSortDto(String jsonString) {
        ObjectMapper obj = new ObjectMapper();
        try {
			return obj.readValue(jsonString, new TypeReference<>() {});
		} catch (Exception e) {
            throw new IllegalArgumentException("Invalid sort parameter");
        }
    }

	@Override
	public ResponseEntity<List<ProductDto>> getProductsByTitleContaining(String title) {
		List<Product> products = productRepository.findByTitleContainingIgnoreCase(title);

		return ResponseEntity.ok(products.stream().map(productMapper::toDto).toList());
	}

	@Override
	public ResponseEntity<List<ProductDto>> getProductsBySellerId(String sellerId) {
		List<Product> products = productRepository.findBySellerId(sellerId);
		return ResponseEntity.status(HttpStatus.OK).body(products.stream().map(productMapper::toDto).toList());
	}
}
