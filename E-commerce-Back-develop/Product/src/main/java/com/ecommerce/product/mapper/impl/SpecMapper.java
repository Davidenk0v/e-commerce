package com.ecommerce.product.mapper.impl;

import org.springframework.stereotype.Component;

import com.ecommerce.product.dto.request.SpecCreationDto;
import com.ecommerce.product.dto.response.SpecDto;
import com.ecommerce.product.entity.Spec;
import com.ecommerce.product.mapper.IDtoMapper;

@Component
public class SpecMapper implements IDtoMapper<SpecCreationDto, Spec, SpecDto>{

	@Override
	public Spec toEntity(SpecCreationDto dto) {
		return Spec.builder()
				.name(dto.name())
				.value(dto.value())
				.build();
	}

	@Override
	public SpecDto toDto(Spec entity) {
		return SpecDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.value(entity.getValue())
				.build();
	}
}
