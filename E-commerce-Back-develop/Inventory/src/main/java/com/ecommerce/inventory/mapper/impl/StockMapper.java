package com.ecommerce.inventory.mapper.impl;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ecommerce.inventory.dto.request.StockCreationDto;
import com.ecommerce.inventory.dto.response.StockDto;
import com.ecommerce.inventory.entity.Stock;
import com.ecommerce.inventory.entity.Warehouse;
import com.ecommerce.inventory.mapper.IDtoMapper;
import com.ecommerce.inventory.repository.WarehouseRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StockMapper implements IDtoMapper<StockCreationDto, Stock, StockDto> {

	private final WarehouseRepository warehouseRepository;
	
	@Override
	public Stock toEntity(StockCreationDto dto) {
        Optional<Warehouse> warehouse = warehouseRepository.findById(dto.warehouseId());
        if(warehouse.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Warehouse with id '%s' does not exist", dto.warehouseId()));
        }
		
		return Stock.builder()
				.modelId(dto.modelId())
				.quantity(dto.quantity())
				.warehouse(warehouse.get())
				.build();
	}

	@Override
	public StockDto toDto(Stock entity) {
		return StockDto.builder()
				.id(entity.getId())
				.modelId(entity.getModelId())
				.quantity(entity.getQuantity())
				.warehouseId(entity.getWarehouse().getId())
				.build();
	}
}

