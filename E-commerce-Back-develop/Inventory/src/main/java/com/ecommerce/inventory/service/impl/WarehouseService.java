package com.ecommerce.inventory.service.impl;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.inventory.dto.request.WarehouseCreationDto;
import com.ecommerce.inventory.dto.response.WarehouseDto;
import com.ecommerce.inventory.entity.Warehouse;
import com.ecommerce.inventory.mapper.IDtoMapper;
import com.ecommerce.inventory.repository.WarehouseRepository;
import com.ecommerce.inventory.service.IWarehouseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseService implements IWarehouseService {
	
	private final WarehouseRepository warehouseRepository;
	private final IDtoMapper<WarehouseCreationDto, Warehouse, WarehouseDto> warehouseMapper;
	
	@Override
	public ResponseEntity<WarehouseDto> saveWarehouse(WarehouseCreationDto warehouseCreationDto) {
		// Check that phone number is not being used
		Optional<Warehouse> optWarehouse = warehouseRepository.findByPhoneNumber(warehouseCreationDto.phoneNumber());
		if (optWarehouse.isPresent()) {
			throw new IllegalArgumentException(
					String.format("Phone number '%s' is already in use", warehouseCreationDto.phoneNumber()));
		}
		
		// Check that email is not being used
		optWarehouse = warehouseRepository.findByEmail(warehouseCreationDto.email());
		if (optWarehouse.isPresent()) {
			throw new IllegalArgumentException(
					String.format("Email '%s' is already in use", warehouseCreationDto.email()));
		}
		
		Warehouse warehouse = warehouseMapper.toEntity(warehouseCreationDto);
		warehouse = warehouseRepository.save(warehouse);
		WarehouseDto commentaryDto = warehouseMapper.toDto(warehouse);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(commentaryDto);
	}

}
