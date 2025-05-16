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

import com.ecommerce.product.dto.request.RatingCreationDto;
import com.ecommerce.product.dto.response.ErrorResponseDto;
import com.ecommerce.product.dto.response.RatingDto;
import com.ecommerce.product.dto.response.RatingWithProductFullNameDto;
import com.ecommerce.product.service.impl.RatingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product/rating")
@RequiredArgsConstructor
@Tag(name = "Rating", description = "Operations related to ratings")
public class RatingController {

    private final RatingService ratingService;

    // Crear una nueva valoración
    @Operation(
        summary = "Crear una nueva valoración",
        description = "Permite crear una valoración para un modelo específico.",
        tags = { "Valoraciones" })
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "201",
                description = "Valoración creada con éxito",
                content = {
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = RatingDto.class))
                }),
        @ApiResponse(
            responseCode = "400",
            description = "Solicitud incorrecta",
            content = {
                @Content(
                    examples = {
                        @ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"Rating with modelId '123' and userId '432u67je-36r6-4532-ac52-e43246489998' already exists\"}")
                    },
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))})
    })
    @PostMapping
    public ResponseEntity<RatingDto> createRating(@RequestBody RatingCreationDto ratingCreationDto) {
        return ratingService.saveRating(ratingCreationDto);
    }

    // Obtener todas las valoraciones para un modelo específico
    @Operation(
        summary = "Obtener las valoraciones de un modelo",
        description = "Recupera todas las valoraciones asociadas a un modelo dado.",
        tags = { "Valoraciones" })
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Valoraciones recuperadas con éxito",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RatingDto.class))
            })
    })
    @GetMapping("/model/{modelId}")
    public ResponseEntity<List<RatingDto>> getRatingsByModelId(
		@Parameter(description = "ID del modelo para obtener sus valoraciones")
		@PathVariable Long modelId) {
        	return ratingService.getRatingsByModelId(modelId);
    }

    // Eliminar una valoración por su ID
    @Operation(
            summary = "Eliminar una valoración",
            description = "Permite eliminar una valoración mediante su ID.",
            tags = { "Valoraciones" })
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Valoración eliminada con éxito",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Valoración no encontrada",
            content = {
                @Content(
                    examples = {
                        @ExampleObject(value = "{\"status\":\"404 NOT_FOUND\",\"message\":\"Rating not found with id '123'\"}")
                    },
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(
		@Parameter(description = "ID de la valoración a eliminar")
		@PathVariable Long id) {
    		return ratingService.deleteRating(id);
    }

    // Actualizar una valoración por su ID
    @Operation(
        summary = "Actualizar una valoración",
        description = "Permite actualizar una valoración existente por su ID.",
        tags = { "Valoraciones" })
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Valoración actualizada con éxito",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RatingDto.class))
                }),
        @ApiResponse(
            responseCode = "404",
            description = "Valoración no encontrada",
            content = {
                @Content(
                    examples = {
                            @ExampleObject(value = "{\"status\":\"404 NOT_FOUND\",\"message\":\"Rating not found with id '123'\"}")
                    },
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDto.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<RatingDto> updateRating(
		@Parameter(description = "ID de la valoración a actualizar")
		@PathVariable Long id,
		@RequestBody RatingDto ratingDto) {
    		return ratingService.updateRating(id, ratingDto);
    }

    @Operation(
        summary = "Obtener las valoraciones de un producto",
        description = "Recupera todas las valoraciones asociadas a los modelos de un producto dado.",
        tags = { "Valoraciones" })
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Valoraciones recuperadas con éxito",
                content = {
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = RatingDto.class))
                })
    })
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<RatingDto>> getRatingsByProductId(@PathVariable Long productId) {
        return ratingService.getAllRatingByProductId(productId);
    }

    @GetMapping("/with-commentary/product/{productId}")
    public ResponseEntity<List<RatingDto>> getRatingsWithCommentaryByProductId(@PathVariable Long productId) {
        return ratingService.getRatingsWithComentaryByProductId(productId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RatingWithProductFullNameDto>> getRatingsByUserId(@PathVariable String userId) {
        return ratingService.getRatingsByUser(userId);
    }

    @GetMapping("/{ratingId}")
    public ResponseEntity<RatingDto> getRatingById(@PathVariable Long ratingId) {
        return ratingService.getRatingById(ratingId);
    }
}
