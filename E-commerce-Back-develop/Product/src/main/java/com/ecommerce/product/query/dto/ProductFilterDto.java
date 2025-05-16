package com.ecommerce.product.query.dto;

import java.util.Set;

import com.ecommerce.product.entity.ModelState;

import lombok.Builder;

@Builder
public record ProductFilterDto(
    Set<String> categories,
    Integer minPrice,
    Integer maxPrice,
    Integer minRating,
    String title,
    ModelState modelState
) {}
