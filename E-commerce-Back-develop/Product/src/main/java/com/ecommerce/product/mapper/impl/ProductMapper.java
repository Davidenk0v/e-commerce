package com.ecommerce.product.mapper.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import com.ecommerce.product.dto.request.ProductCreationDto;
import com.ecommerce.product.dto.response.ProductDto;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Image;
import com.ecommerce.product.entity.Model;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.entity.Rating;
import com.ecommerce.product.mapper.IDtoMapper;
import com.ecommerce.product.query.dto.ProductFilterDto;
import com.ecommerce.product.repository.ModelRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductMapper implements IDtoMapper<ProductCreationDto, Product, ProductDto> {
	
    private final ModelRepository modelRepository;

    @Override
    public Product toEntity(ProductCreationDto dto) {
        return Product.builder()
                .title(dto.title())
                .manufacturer(dto.manufacturer())
                .sellerId(dto.sellerId())
                .build();
    }

    @Override
    public ProductDto toDto(Product entity) {
        List<Long> categoryIds = entity.getCategories().stream().map(Category::getId).toList();
        List<Model> productModels = modelRepository.findByProductId(entity.getId());
        List<Long> modelIds = new ArrayList<>();
        
        // This fields depend on the product having at least one model related
        Model model = null;
        Long currentModelId = null;
        String imageUrl = null;
        String description = null;
        String shortDescription = null;
        BigDecimal price = null;
        Integer opinionCount = null;
        BigDecimal rating = null;
        
        if (!productModels.isEmpty()) {
        	model = productModels.get(0);
        	currentModelId = model.getId();
        	modelIds = productModels.stream().map(Model::getId).toList();
        	description = model.getDescription();
        	shortDescription = getH2Content(model.getDescription());
        	price = model.getPrice();
        	opinionCount = calculateOpinionCount(entity);
        	rating = calculateRating(entity);
        	List<Image> images = model.getImages();
			if (!images.isEmpty()) {
				imageUrl = images.get(0).getUrl();
			}
		}

        return ProductDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .categoryIds(categoryIds)
                .description(description)
                .shortDescription(shortDescription)
                .imageUrl(imageUrl)
                .manufacturer(entity.getManufacturer())
                .price(price)
                .modelIds(modelIds)
                .currentModelId(currentModelId)
                .opinionCount(opinionCount)
                .rating(rating)
                .sellerId(entity.getSellerId())
                .build();
    }

    // Calculate the average rating of a product
    private BigDecimal calculateRating(Product entity) {
        List<Rating> ratings = entity.getModels().stream()
                .flatMap(model -> model.getRatings().stream())
                .toList();

        if (ratings.isEmpty()) {
            return BigDecimal.ZERO;
        }

        int sum = ratings.stream()
                .mapToInt(Rating::getValue)
                .sum();

        return new BigDecimal(sum).divide(BigDecimal.valueOf(ratings.size()), 2, RoundingMode.HALF_UP);
    }
    
    // Calculate the amount of ratings of a product
    private Integer calculateOpinionCount(Product entity) {
        List<Rating> ratings = entity.getModels().stream()
                .flatMap(model -> model.getRatings().stream())
                .toList();

        if (ratings.isEmpty()) {
            return 0;
        }

        return ratings.size();
    }
    
    // Get the content of the first H2 tag in the product description
	private String getH2Content(String description) {
		Document document = Jsoup.parse(description);
		Element h2Element = document.selectFirst("h2");
		return (h2Element != null) ? h2Element.text() : "";
	}
    
    // This method is used to convert a product entity to a product DTO taking filter criteria into account
    public ProductDto toDto(Product entity, ProductFilterDto productFilterDto) {
    	Integer minPrice = productFilterDto.minPrice();
    	Integer maxPrice = productFilterDto.maxPrice();
        List<Long> categoryIds = entity.getCategories().stream().map(Category::getId).toList();
        List<Model> productModels = null;
		if (minPrice != null && maxPrice != null) {
			productModels = modelRepository.findByProductIdAndPriceBetween(entity.getId(), minPrice, maxPrice);
		} else if (minPrice != null) {
			productModels = modelRepository.findByProductIdAndPriceGreaterThanEqual(entity.getId(), minPrice);
		} else if (maxPrice != null) {
			productModels = modelRepository.findByProductIdAndPriceLessThanEqual(entity.getId(), maxPrice);
		} else {
			productModels = modelRepository.findByProductId(entity.getId());
		}
        
        List<Long> modelIds = new ArrayList<>();
        
        // This fields depend on the product having at least one model related
        Model model = null;
        Long currentModelId = null;
        String imageUrl = null;
        String description = null;
        String shortDescription = null;
        BigDecimal price = null;
        Integer opinionCount = null;
        BigDecimal rating = null;
        if (!productModels.isEmpty()) {
        	model = productModels.get(0);
        	currentModelId = model.getId();
        	modelIds = productModels.stream().map(Model::getId).toList();
        	description = model.getDescription();
        	shortDescription = getH2Content(model.getDescription());
        	price = model.getPrice();
        	opinionCount = calculateOpinionCount(entity);
        	rating = calculateRating(entity);
        	List<Image> images = model.getImages();
			if (!images.isEmpty()) {
				imageUrl = images.get(0).getUrl();
			}
		}

        return ProductDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .categoryIds(categoryIds)
                .description(description)
                .shortDescription(shortDescription)
                .imageUrl(imageUrl)
                .manufacturer(entity.getManufacturer())
                .price(price)
                .modelIds(modelIds)
                .currentModelId(currentModelId)
                .opinionCount(opinionCount)
                .rating(rating)
                .sellerId(entity.getSellerId())
                .build();
    }
}
