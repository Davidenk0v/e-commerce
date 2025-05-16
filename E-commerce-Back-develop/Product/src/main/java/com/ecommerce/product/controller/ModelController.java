package com.ecommerce.product.controller;

import java.util.List;

import com.ecommerce.product.entity.Model;
import com.ecommerce.product.entity.ModelState;
import org.bouncycastle.math.raw.Mod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.product.dto.request.ModelCreationDto;
import com.ecommerce.product.dto.response.ErrorResponseDto;
import com.ecommerce.product.dto.response.ModelDto;
import com.ecommerce.product.service.IModelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product/model")
@RequiredArgsConstructor
@Tag(name = "Model", description = "Operations related to models")
public class ModelController {

	private final IModelService modelService;
	
	@ApiResponses(value = {
        @ApiResponse(
        	responseCode = "201", 
			description="Model created successfully"),
        @ApiResponse(
        	responseCode = "400", 
        	description = "Invalid request body",
            content = {
            	@Content(
            		examples = {@ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"No id for the related product was provided\",\"details\":\"uri=/api/v1/model\"}")},
            		mediaType = "application/json",
            		schema = @Schema(implementation = ErrorResponseDto.class))})})
    @Operation(
    	summary = "Create a new model",
    	description = "Creates a new model related to an existing product")
	@PostMapping
	public ResponseEntity<ModelDto> saveModel(@RequestBody ModelCreationDto modelCreationDto) {
		return modelService.saveModel(modelCreationDto);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Model found"),
		@ApiResponse(
			responseCode = "404", 
			description = "Model not found",
			content = {
				@Content(schema = @Schema(implementation = Void.class))})})
	@Operation(
		summary = "Get a model by id", 
		description = "Returns the Model with the given id")
	@GetMapping("/{id}")
	public ResponseEntity<ModelDto> getModel(@PathVariable Long id) {
		return modelService.getModel(id);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Models found", 
			content = {@Content(examples = {
				@ExampleObject(value = "[{\"id\":1,\"productId\":1,\"price\":2000.0,\"imagesUrl\":[\"1657783580839-portatil.png\"],\"description\":\"Portátil de última generación en negro azabache, estética lujosa y alta capacidad con 32GB de RAM y memoria SSD de 1TB.\",\"state\":\"PENDING_APPROVAL\",\"commentaries\":[{\"id\":1,\"userId\":\"569g65de-35y6-4552-ac54-e52085109818\",\"modelId\":1,\"text\":\"Excelente portátil, muy contento con la compra.\",\"creationDate\":\"24/03/2025\",\"images\":[],\"likes\":1,\"parentCommentaryId\":null,\"childCommentaryIds\":[]},{\"id\":2,\"userId\":\"555f66de-36b6-4332-ac51-e52012661623\",\"modelId\":1,\"text\":\"Muy buena calidad, el envío fue rápido.\",\"creationDate\":\"24/03/2025\",\"images\":[],\"likes\":2,\"parentCommentaryId\":null,\"childCommentaryIds\":[]}]}]")})})})
	@Operation(
		summary = "Get all models", 
		description = "Returns all models")
	@GetMapping
	public ResponseEntity<List<ModelDto>> getModels() {
		return modelService.getModels();
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "204", 
			description = "Model deleted"),
		@ApiResponse(
			responseCode = "404", 
			description = "Model not found")})
	@Operation(
		summary = "Delete a Model by id", 
		description = "Deletes the Model with the given id")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
		return modelService.deleteModel(id);
	}

	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Model updated"),
		@ApiResponse(
			responseCode = "404", 
			description = "Model not found",
			content = {
				@Content(schema = @Schema(implementation = Void.class))}),
		@ApiResponse(
        	responseCode = "400", 
        	description = "Invalid request body",
            content = {
            	@Content(
            		examples = {@ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"Spec with id '1' does not exist\",\"details\":\"uri=/api/v1/model\"}")},
            		mediaType = "application/json",
            		schema = @Schema(implementation = ErrorResponseDto.class))})})
	@Operation(
		summary = "Updates a Model by id", 
		description = "Updates the Model with the given id and changes its state to 'PENDING_APPROVAL'")
	@PutMapping("/{id}")
	public ResponseEntity<ModelDto> updateModel(@PathVariable Long id, @RequestBody ModelCreationDto modelCreationDto) {
		return modelService.updateModel(id, modelCreationDto);
	}

	@PutMapping("/{modelId}/state")
	public ResponseEntity<ModelDto> changeModelState(@PathVariable Long modelId, @RequestBody ModelState modelState) {
		return modelService.changesStateModel(modelId, modelState);
	}

	@PutMapping("/approve-all")
	public ResponseEntity<List<ModelDto>> approveAllModels(@RequestBody List<Long> ids) {
		return modelService.approveModels(ids);
	}

	@PutMapping("/reject-all")
	public ResponseEntity<List<ModelDto>> rejectAllModels(@RequestBody List<Long> ids) {
		return modelService.rejectModels(ids);
	}
}
