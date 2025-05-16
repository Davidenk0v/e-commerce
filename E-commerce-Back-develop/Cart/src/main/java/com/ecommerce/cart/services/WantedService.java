package com.ecommerce.cart.services;

import com.ecommerce.cart.dto.response.ProductDetailsDto;
import com.ecommerce.cart.dto.response.WantedDto;
import com.ecommerce.cart.entities.Wanted;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WantedService {



    ResponseEntity<WantedDto> addToWantedList(Long modelId, String userId);

    ResponseEntity<WantedDto> removeFromWantedList(Long productId, String userId);

    ResponseEntity<String> clearWantedList(String userId);

    ResponseEntity<List<WantedDto>> getWantedList(String userId);

}
