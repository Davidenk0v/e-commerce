package com.ecommerce.inventory.mapper;

public interface IDtoMapper<C, E, D> {

	E toEntity(C dto);

	D toDto(E entity);
}
