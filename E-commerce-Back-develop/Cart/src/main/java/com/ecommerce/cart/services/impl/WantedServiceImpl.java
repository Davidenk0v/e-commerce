package com.ecommerce.cart.services.impl;

import com.ecommerce.cart.dto.response.WantedDto;
import com.ecommerce.cart.entities.Wanted;
import com.ecommerce.cart.mapper.IDtoMapper;
import com.ecommerce.cart.repositories.ProductDetailsRepository;
import com.ecommerce.cart.repositories.WantedRepository;
import com.ecommerce.cart.services.WantedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WantedServiceImpl implements WantedService {

    private final WantedRepository wantedRepository;

    private final IDtoMapper<Wanted, WantedDto> dtoMapper;

    @Override
    public ResponseEntity<WantedDto> addToWantedList(Long modelId, String userId) {
        Wanted wanted = Wanted.builder()
                .modelId(modelId)
                .userId(userId)
                .build();
        try {
            wantedRepository.save(wanted);
            return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.entityToDto(wanted));
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException(
                    "Error al añadir el producto a la lista de deseados"
            );
        }
    }

    @Override
    public ResponseEntity<WantedDto> removeFromWantedList(Long productId, String userId) {
        Optional<Wanted> wanted = wantedRepository.findByUserIdAndModelId(userId, productId);
        if(wanted.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Product with id %s not found in wanted list", productId)
            );
        }
        try {
            wantedRepository.delete(wanted.get());
            return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.entityToDto(wanted.get()));
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Error removing product from wanted list");
        }

    }

    @Override
    public ResponseEntity<String> clearWantedList(String userId) {
        List<Wanted> wantedList = wantedRepository.findByUserId(userId);
        if(wantedList.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Wanted list for user %s is empty", userId)
            );
        }
        try {
            wantedRepository.deleteAll(wantedList);
            return ResponseEntity.status(HttpStatus.OK).body("Wanted list cleared");
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Error clearing wanted list");
        }
    }


    @Override
    public ResponseEntity<List<WantedDto>> getWantedList(String userId) {
        List<Wanted> wantedList = wantedRepository.findByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.entityListToResponseDtoList(wantedList));
    }


}
