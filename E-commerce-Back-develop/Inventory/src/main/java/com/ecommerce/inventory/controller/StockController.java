package com.ecommerce.inventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.inventory.dto.request.StockCreationDto;
import com.ecommerce.inventory.dto.response.ErrorResponseDto;
import com.ecommerce.inventory.dto.response.StockDto;
import com.ecommerce.inventory.service.IStockService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/inventory/stock")
@RequiredArgsConstructor
@Tag(name = "Stock", description = "Operations related to stocks")
public class StockController {

	private final IStockService stockService;
	
	@ApiResponses(value = {
        @ApiResponse(
        	responseCode = "201", 
			description="Stock created successfully"),
        @ApiResponse(
        	responseCode = "400", 
        	description = "Invalid request body",
            content = {
            	@Content(
            		examples = {@ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"Warehouse with id  '1' does not exist\",\"details\":\"uri=/api/v1/inventory/stock\"}")},
            		mediaType = "application/json",
            		schema = @Schema(implementation = ErrorResponseDto.class))})})
	    @Operation(
	    	summary = "Create a new stock",
	    	description = "Creates a stock after checking that the model and the warehouse provided exist and that there is no stock already registered for the given model at the given warehouse")
	@PostMapping
	public ResponseEntity<StockDto> saveWarehouse(@RequestBody StockCreationDto stockCreationDto) {
		return stockService.saveStock(stockCreationDto);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Stock found"),
		@ApiResponse(
			responseCode = "404", 
			description = "Stock not found",
			content = {
				@Content(schema = @Schema(implementation = Void.class))})})
	@Operation(
		summary = "Get the stock for a given model in a given warehouse", 
		description = "Returns the stock for the given model in the given warehouse")
	@GetMapping("/model/{modelId}/warehouse/{warehouseId}")
	public ResponseEntity<StockDto> getStock(
		@PathVariable Long modelId, 
		@PathVariable Long warehouseId) {
			return stockService.getStock(warehouseId, modelId);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Total stock found")})
		@Operation(
			summary = "Get the total stock for a given model across all warehouses", 
			description = "Returns the stock available for the given model across all warehouses")
		@GetMapping("/total/{modelId}")
		public ResponseEntity<Long> getTotalStock(@PathVariable Long modelId) {
				return stockService.getTotalStock(modelId);
		}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Stock updated"),
		@ApiResponse(
			responseCode = "404", 
			description = "Stock not found",
			content = {
				@Content(schema = @Schema(implementation = Void.class))})})
	@Operation(
		summary = "Updates stock amount by id", 
		description = "Updates the amount of stock of a model in a warehouse by the id of the stock")
	@PutMapping("/{id}")
	public ResponseEntity<StockDto> updateStock(@PathVariable Long id, @RequestBody StockCreationDto stockCreationDto) {
		return stockService.updateStock(id, stockCreationDto);
	}
}
