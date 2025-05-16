package com.ecommerce.product.dto.request;

import com.ecommerce.product.dto.response.ProductDto;
import com.ecommerce.product.entity.ERequestState;
import lombok.Builder;

import java.util.List;

@Builder
public record SellerRequestDto(
                               Long id,
                               List<Long> productIds,
                               String userId,
                               ERequestState status,
                               List<String> documentURLs) {
}
