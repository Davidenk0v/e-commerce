package com.ecommerce.cart.dto.response;

import com.ecommerce.cart.entities.ProductDetails;
import lombok.Builder;

import java.util.List;

@Builder
public record CartDto(
      Long id,

      String userId,

      List<ProductDetailsDto> productsDetails
) {
}
