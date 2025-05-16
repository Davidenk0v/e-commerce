package com.ecommerce.product.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.product.dto.request.SpecCreationDto;
import com.ecommerce.product.dto.response.ErrorResponseDto;
import com.ecommerce.product.dto.response.SpecDto;
import com.ecommerce.product.service.ISpecService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product/spec")
@RequiredArgsConstructor
@Tag(name = "Spec", description = "Operations related to specs")
public class SpecController {

	private final ISpecService specService;
	
	@ApiResponses(value = {
        @ApiResponse(
        	responseCode = "201", 
			description="Spec created successfully"),
        @ApiResponse(
        	responseCode = "400", 
        	description = "Invalid request body",
            content = {
            	@Content(
            		examples = {@ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"Spec with name 'color' and value 'rojo' already exists\",\"details\":\"uri=/api/v1/spec\"}")},
            		mediaType = "application/json",
            		schema = @Schema(implementation = ErrorResponseDto.class))})})
    @Operation(
    	summary = "Create a new spec",
    	description = "Name and value combination must be unique among all categories")
	@PostMapping
	public ResponseEntity<SpecDto> saveSpec(@RequestBody SpecCreationDto specCreationDto) {
		return specService.saveSpec(specCreationDto);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Spec found"),
		@ApiResponse(
			responseCode = "404", 
			description = "Spec not found",
			content = {
				@Content(schema = @Schema(implementation = Void.class))})})
	@Operation(
		summary = "Get a spec by id", 
		description = "Returns the spec with the given id")
	@GetMapping("/{id}")
	public ResponseEntity<SpecDto> getSpec(@PathVariable Long id) {
		return specService.getSpec(id);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Specs found", 
			content = {@Content(examples = {
				@ExampleObject(value = "[{\"id\":1,\"name\":\"color\",\"value\":\"rojo\"},{\"id\":2,\"name\":\"color\",\"value\":\"azul\"}]")})})})
	@Operation(
		summary = "Get all specs allowing filtering by name", 
		description = "Returns all specs whose name is the given name, if no name is given, returns all specs")
	@GetMapping
	public ResponseEntity<List<SpecDto>> getSpecsByName(@RequestParam Optional<String> name) {
		return specService.getSpecsByName(name);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "204", 
			description = "Spec deleted"),
		@ApiResponse(
			responseCode = "404", 
			description = "Spec not found")})
	@Operation(
		summary = "Delete a spec by id", 
		description = "Deletes the spec with the given id")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSpec(@PathVariable Long id) {
		return specService.deleteSpec(id);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Spec updated"),
		@ApiResponse(
			responseCode = "404", 
			description = "Spec not found",
			content = {
				@Content(schema = @Schema(implementation = Void.class))}),
		@ApiResponse(
        	responseCode = "400", 
        	description = "Invalid request body",
            content = {
            	@Content(
            		examples = {@ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"Spec with name 'color' and value 'rojo' already exists\",\"details\":\"uri=/api/v1/spec\"}")},
            		mediaType = "application/json",
            		schema = @Schema(implementation = ErrorResponseDto.class))})})
	@Operation(
			summary = "Updates a spec by id", 
			description = "Updates the spec with the given id, checking first if the new combination of name and value is available")
	@PutMapping("/{id}")
	public ResponseEntity<SpecDto> updateSpec(@PathVariable Long id, @RequestBody SpecCreationDto specCreationDto) {
		return specService.updateSpec(id, specCreationDto);
	}	
}
