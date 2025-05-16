package com.ecommerce.inventory.service;

import org.springframework.http.ResponseEntity;

import com.ecommerce.inventory.dto.request.WarehouseCreationDto;
import com.ecommerce.inventory.dto.response.WarehouseDto;

public interface IWarehouseService {
	
	ResponseEntity<WarehouseDto> saveWarehouse(WarehouseCreationDto categoryCreationDto);
}
