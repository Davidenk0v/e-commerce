package com.ecommerce.inventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.inventory.dto.request.WarehouseCreationDto;
import com.ecommerce.inventory.dto.response.ErrorResponseDto;
import com.ecommerce.inventory.dto.response.WarehouseDto;
import com.ecommerce.inventory.service.IWarehouseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/inventory/warehouse")
@RequiredArgsConstructor
@Tag(name = "Warehouse", description = "Operations related to warehouses")
public class WarehouseController {

	private final IWarehouseService warehouseService;
	
	@ApiResponses(value = {
        @ApiResponse(
        	responseCode = "201", 
			description="Warehouse created successfully"),
        @ApiResponse(
        	responseCode = "400", 
        	description = "Invalid request body",
            content = {
            	@Content(
            		examples = {@ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"Phone number '+34666777888' is already in use\",\"details\":\"uri=/api/v1/inventory/warehouse\"}")},
            		mediaType = "application/json",
            		schema = @Schema(implementation = ErrorResponseDto.class))})})
	    @Operation(
	    	summary = "Create a new warehouse",
	    	description = "Phone number and email must be unique among all warehouses")
	@PostMapping
	public ResponseEntity<WarehouseDto> saveWarehouse(@RequestBody WarehouseCreationDto warehouseCreationDto) {
		return warehouseService.saveWarehouse(warehouseCreationDto);
	}	
}
