package com.ecommerce.product.dto.response;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ProductDto(
	@Schema(example = "1")
	Long id,
	@Schema(example = "Iphone 10")
	String title,
	@Schema(example="Apple")
	String manufacturer,
	@Schema(example="569f67de-36e6-4552-ac54-e52085109818")
	String sellerId,
	@Schema(example="[1, 2]")
	List<Long> categoryIds,
	@Schema(example="[1, 2]")
	List<Long> modelIds,
	@Schema(example="1")
	Long currentModelId,
	@Schema(example="1657783580839-iphone10.png")
	String imageUrl,
	@Schema(example="799.99")
	BigDecimal price,
	@Schema(example="<h2>Iphone 10 with 256GB of storage and 6GB of RAM in black</h2>")
	String description,
	@Schema(example="Iphone 10 with 256GB of storage and 6GB of RAM in black")
	String shortDescription,
	@Schema(example="4.5")
	BigDecimal rating,
	@Schema(example="104")
	Integer opinionCount
) {}
