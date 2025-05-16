package com.ecommerce.cart.mapper;

import org.springframework.http.HttpStatus;

import java.util.List;

public interface IDtoMapper<E, D> {

    E dtoToEntity(D dto);

    D entityToDto(E entity);

   // R entityToResponseDto(E entity, String message, HttpStatus status);

    List<D> entityListToResponseDtoList(List<E> entityList);
}
