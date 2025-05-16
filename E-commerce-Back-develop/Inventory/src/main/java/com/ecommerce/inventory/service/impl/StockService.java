package com.ecommerce.inventory.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.inventory.client.ProductClient;
import com.ecommerce.inventory.dto.request.StockCreationDto;
import com.ecommerce.inventory.dto.response.ModelDto;
import com.ecommerce.inventory.dto.response.StockDto;
import com.ecommerce.inventory.entity.Stock;
import com.ecommerce.inventory.entity.Warehouse;
import com.ecommerce.inventory.mapper.IDtoMapper;
import com.ecommerce.inventory.repository.StockRepository;
import com.ecommerce.inventory.repository.WarehouseRepository;
import com.ecommerce.inventory.service.IStockService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService implements IStockService {

	private final StockRepository stockRepository;
	private final WarehouseRepository warehouseRepository;
	private final ProductClient productClient;
	private final IDtoMapper<StockCreationDto, Stock, StockDto> stockMapper;
	
	@Override
	public ResponseEntity<StockDto> saveStock(StockCreationDto stockCreationDto) {
		// Check that the warehouse exists
		Optional<Warehouse> optWarehouse = warehouseRepository.findById(stockCreationDto.warehouseId());
		if (optWarehouse.isEmpty()) {
			throw new IllegalArgumentException(
					String.format("Warehouse with id '%s' does not exist", stockCreationDto.warehouseId()));
		}
		
		//Check that the model exists
		ModelDto model = productClient.getModel(stockCreationDto.modelId());
		if (model == null) {
			throw new IllegalArgumentException(
					String.format("Model with id '%s' does not exist", stockCreationDto.modelId()));
		}
		
		// Check that there is no registered stock for the given warehouse
		Optional<Stock> optStock = stockRepository.findByWarehouseIdAndModelId(stockCreationDto.warehouseId(), stockCreationDto.modelId());
		if (optStock.isPresent()) {
			throw new IllegalArgumentException(
					String.format("There is already a stock registered for the model with id '%s' in the warehouse with id '%s'", 
							stockCreationDto.modelId(), 
							stockCreationDto.warehouseId()));
		}
		
		Stock stock = stockMapper.toEntity(stockCreationDto);
		stock = stockRepository.save(stock);
		StockDto stockDto = stockMapper.toDto(stock);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(stockDto);
	}

	@Override
	public ResponseEntity<StockDto> getStock(Long warehouseId, Long modelId) {
		Optional<Stock> stockOptional = stockRepository.findByWarehouseIdAndModelId(warehouseId, modelId);
		if (stockOptional.isPresent()) {
			StockDto stockDto = stockMapper.toDto(stockOptional.get());
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(stockDto);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@Override
	public ResponseEntity<StockDto> updateStock(Long id, StockCreationDto stockCreationDto) {
		Optional<Stock> stockOptional = stockRepository.findById(id);
		if (!stockOptional.isPresent() ) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();	
		}
		
		Stock stock = stockOptional.get();
		stock.setQuantity(stockCreationDto.quantity());
		stock = stockRepository.save(stock);
		StockDto stockDto = stockMapper.toDto(stock);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(stockDto);
	}

	@Override
	public ResponseEntity<Long> getTotalStock(Long modelId) {
		List<Stock> stocks = stockRepository.findByModelId(modelId);
		
		if (stocks.isEmpty()) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(0L);
		}
		
		Long totalStock = stocks.stream().mapToLong(Stock::getQuantity).sum();
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(totalStock);
	}
	
	
}
