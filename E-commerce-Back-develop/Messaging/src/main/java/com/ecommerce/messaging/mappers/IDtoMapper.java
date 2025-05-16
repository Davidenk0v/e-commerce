package com.ecommerce.messaging.mappers;

public interface IDtoMapper<C, E, D> {

	E toEntity(C dto);

	D toDto(E entity);
}
