package com.ecommerce.inventory.mapper.impl;

import org.springframework.stereotype.Component;

import com.ecommerce.inventory.dto.request.WarehouseCreationDto;
import com.ecommerce.inventory.dto.response.WarehouseDto;
import com.ecommerce.inventory.entity.Warehouse;
import com.ecommerce.inventory.mapper.IDtoMapper;

@Component
public class WarehouseMapper implements IDtoMapper<WarehouseCreationDto, Warehouse, WarehouseDto> {

	@Override
	public Warehouse toEntity(WarehouseCreationDto dto) {
		return Warehouse.builder()
				.localization(dto.localization())
				.description(dto.description())
				.email(dto.email())
				.phoneNumber(dto.phoneNumber())
				.build();
	}

	@Override
	public WarehouseDto toDto(Warehouse entity) {
		return WarehouseDto.builder()
				.id(entity.getId())
				.localization(entity.getLocalization())
				.description(entity.getDescription())
				.email(entity.getEmail())
				.phoneNumber(entity.getPhoneNumber())
				.build();
	}
}

