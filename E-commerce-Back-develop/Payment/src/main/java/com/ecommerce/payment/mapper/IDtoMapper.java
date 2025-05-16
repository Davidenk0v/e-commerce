package com.ecommerce.payment.mapper;

public interface IDtoMapper<C, E, D> {

	E toEntity(C dto);

	D toDto(E entity);
}
