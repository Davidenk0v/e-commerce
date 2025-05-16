package com.ecommerce.product.dto.response;

import com.ecommerce.product.dto.request.UserDto;
import com.ecommerce.product.entity.ERequestState;
import lombok.Builder;


import java.util.List;
@Builder
public record SellerRequestResponseDto(
                               Long id,
                               List<ModelDto>modelList,
                               UserDto user,
                               ERequestState status,
                               List<String> documentURLs){
}
