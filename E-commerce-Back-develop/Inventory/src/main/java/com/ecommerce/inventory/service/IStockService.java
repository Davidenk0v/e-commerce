package com.ecommerce.inventory.service;

import org.springframework.http.ResponseEntity;

import com.ecommerce.inventory.dto.request.StockCreationDto;
import com.ecommerce.inventory.dto.response.StockDto;

public interface IStockService {

	ResponseEntity<StockDto> saveStock(StockCreationDto stockCreationDto);
	
	ResponseEntity<StockDto> getStock(Long warehouseId, Long modelId);
	
	ResponseEntity<StockDto> updateStock(Long id, StockCreationDto stockCreationDto);

	ResponseEntity<Long> getTotalStock(Long modelId);
}
