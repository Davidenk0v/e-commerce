package com.ecommerce.product.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ecommerce.product.dto.request.CommentaryCreationDto;
import com.ecommerce.product.dto.request.ModelCreationDto;
import com.ecommerce.product.dto.request.SpecCreationDto;
import com.ecommerce.product.dto.response.CommentaryDto;
import com.ecommerce.product.dto.response.ModelDto;
import com.ecommerce.product.dto.response.SpecDto;
import com.ecommerce.product.entity.Commentary;
import com.ecommerce.product.entity.Image;
import com.ecommerce.product.entity.Model;
import com.ecommerce.product.entity.ModelState;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.entity.Spec;
import com.ecommerce.product.mapper.IDtoMapper;
import com.ecommerce.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ModelMapper implements IDtoMapper<ModelCreationDto, Model, ModelDto> {

    private final ProductRepository productRepository;
    private final IDtoMapper<CommentaryCreationDto, Commentary, CommentaryDto> commentaryMapper;
    private final IDtoMapper<SpecCreationDto, Spec, SpecDto> specMapper;


    @Override
    public Model toEntity(ModelCreationDto dto) {
        Optional<Product> product = productRepository.findById(dto.productId());
        if(product.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Product with id '%s' does not exist", dto.productId()));
        }
        
        return Model.builder()
                .description(dto.description())
                .price(dto.price())
                .product(product.get())
                .state(ModelState.PENDING_APPROVAL)
                .build();
    }

    @Override
    public ModelDto toDto(Model entity) {
    	// Check if there  are images related to the model and if so get their urls
    	List<Image> images = entity.getImages();
    	List<String> imageUrls = new ArrayList<>();
    	if (images != null) {
    		imageUrls = images.stream().map(Image::getUrl).toList();
    	}
    	
    	// Check if there are commentaries related to the model and if so convert them to DTOs
    	List<Commentary> commentaries = entity.getCommentaries();
    	List<CommentaryDto> commentaryDtos = new ArrayList<>();
		if (commentaries != null) {
	    	for (Commentary commentary : commentaries) {
	    		commentaryDtos.add(commentaryMapper.toDto(commentary));
	    	}
		}
		
    	// Check if there are specs related to the model and if so convert them to DTOs
    	List<Spec> specs = entity.getSpecs();
    	List<SpecDto> specDtos = new ArrayList<>();
		if (specs != null) {
	    	for (Spec spec : specs) {
	    		specDtos.add(specMapper.toDto(spec));
	    	}
		}
		String productName = null;
		if(productRepository.findById(entity.getProduct().getId()).isPresent()) {
			productName = productRepository.findById(entity.getProduct().getId()).get().getTitle();
		}
        return ModelDto.builder()
                .id(entity.getId())
                .imagesUrl(imageUrls)
                .commentaries(commentaryDtos)
                .specs(specDtos)
                .state(entity.getState())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .productId(entity.getProduct().getId())
				.productName(productName)
                .build();
    }


}
