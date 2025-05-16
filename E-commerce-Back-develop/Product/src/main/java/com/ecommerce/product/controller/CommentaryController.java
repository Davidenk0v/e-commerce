package com.ecommerce.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.product.dto.request.CommentaryCreationDto;
import com.ecommerce.product.dto.response.CommentaryDto;
import com.ecommerce.product.dto.response.ErrorResponseDto;
import com.ecommerce.product.service.ICommentaryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product/commentary")
@RequiredArgsConstructor
@Tag(name = "Commentary", description = "Operations related to commentaries")
public class CommentaryController {

	private final ICommentaryService commentaryService;
	
	@ApiResponses(value = {
        @ApiResponse(
        	responseCode = "201", 
			description="Commentary created successfully"),
        @ApiResponse(
        	responseCode = "400", 
        	description = "Invalid request body",
            content = {
            	@Content(
            		examples = {@ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"Referenced Commentary with id '1' does not exist\",\"details\":\"uri=/api/v1/commentary\"}")},
            		mediaType = "application/json",
            		schema = @Schema(implementation = ErrorResponseDto.class))})})
    @Operation(
    	summary = "Create a new Commentary",
    	description = "Commentaries made by the users related to a model or a previous commentary")
	@PostMapping
	public ResponseEntity<CommentaryDto> saveCommentary(@RequestBody CommentaryCreationDto commentaryCreationDto) {
		return commentaryService.saveCommentary(commentaryCreationDto);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Commentary found"),
		@ApiResponse(
			responseCode = "404", 
			description = "Commentary not found",
			content = {
				@Content(schema = @Schema(implementation = Void.class))})})
	@Operation(
		summary = "Get a Commentary by id", 
		description = "Returns the Commentary with the given id")
	@GetMapping("/{id}")
	public ResponseEntity<CommentaryDto> getCommentary(@PathVariable Long id) {
		return commentaryService.getCommentary(id);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "204", 
			description = "Commentary deleted"),
		@ApiResponse(
			responseCode = "404", 
			description = "Commentary not found")})
	@Operation(
		summary = "Delete a Commentary by id", 
		description = "Deletes the Commentary with the given id")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCommentary(@PathVariable Long id) {
		return commentaryService.deleteCommentary(id);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Commentary updated"),
		@ApiResponse(
			responseCode = "404", 
			description = "Commentary not found",
			content = {
				@Content(schema = @Schema(implementation = Void.class))})})
	@Operation(
		summary = "Updates a Commentary by id", 
		description = "Updates the Commentary with the given id")
	@PutMapping("/{id}")
	public ResponseEntity<CommentaryDto> updateCommentary(@PathVariable Long id, @RequestBody CommentaryCreationDto commentaryCreationDto) {
		return commentaryService.updateCommentary(id, commentaryCreationDto);
	}

	@GetMapping("/parent-id/{parentId}")
	public ResponseEntity<?> getCommentariesByIds(@PathVariable Long parentId) {
		return commentaryService.getAllCommentariesByParentId(parentId);
	}
	
	@GetMapping("/as-question/product-id/{productId}")
	public ResponseEntity<List<CommentaryDto>> getCommentariesByModelProductIdAndRatingNullAndParentCommentaryNull(@PathVariable Long productId) {
		return commentaryService.getCommentariesByModelProductIdAndRatingNullAndParentCommentaryNull(productId);
	}
	
	@GetMapping("/as-question/user-id/{userId}")
	public ResponseEntity<List<CommentaryDto>> getCommentariesByUserIdAndRatingNullAndParentCommentaryNull(
			@PathVariable String userId) {
		return commentaryService.getCommentariesByUserIdAndRatingNullAndParentCommentaryNull(userId);
	}
}
