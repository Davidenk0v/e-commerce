package com.ecommerce.product.controller;

import com.ecommerce.product.dto.request.LikeCreationDto;
import com.ecommerce.product.dto.response.LikeDto;
import com.ecommerce.product.dto.response.ErrorResponseDto;
import com.ecommerce.product.service.ILikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product/like")
@RequiredArgsConstructor
@Tag(name = "Like", description = "Operations related to likes")
public class LikeController {

    private final ILikeService likeService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Like created successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = LikeDto.class),
                examples = @ExampleObject(value = "{\"id\":1, \"userId\":\"user1\", \"commentaryId\":1}"))),
        @ApiResponse(responseCode = "400", description = "Invalid request body",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\", \"message\":\"Invalid like creation request\", \"details\":\"uri=/api/v1/like\"}")))
    })
    @Operation(summary = "Create a new like", description = "Creates a new like for a commentary")
    @PostMapping
    public ResponseEntity<LikeDto> saveLike(@RequestBody LikeCreationDto likeCreationDto) {
        return likeService.saveLike(likeCreationDto);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Likes found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = LikeDto.class),
                examples = @ExampleObject(value = "[{\"id\":1, \"userId\":\"user1\", \"commentaryId\":1}, {\"id\":2, \"userId\":\"user2\", \"commentaryId\":1}]")))
    })
    @Operation(summary = "Get all likes", description = "Returns all likes")
    @GetMapping
    public ResponseEntity<List<LikeDto>> getLikes() {
        return likeService.getLikes();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Likes found for user",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = LikeDto.class),
                examples = @ExampleObject(value = "[{\"id\":1, \"userId\":\"user1\", \"commentaryId\":1}, {\"id\":3, \"userId\":\"user1\", \"commentaryId\":2}]")))
    })
    @Operation(summary = "Get likes by user ID", description = "Returns all likes for a specific user")
    @GetMapping("/user/{id}")
    public ResponseEntity<List<LikeDto>> getLikesByUserId(@PathVariable String id) {
        return likeService.getLikesByUser(id);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Likes found for commentary",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = LikeDto.class),
                examples = @ExampleObject(value = "[{\"id\":1, \"userId\":\"user1\", \"commentaryId\":1}, {\"id\":2, \"userId\":\"user2\", \"commentaryId\":1}]"))),
        @ApiResponse(
        	responseCode = "400", 
        	description = "Invalid request body",
            content = {
            	@Content(
            		examples = {@ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"Commentary with id '1' not found\",\"details\":\"uri=/api/v1/like/commentary/1\"}")},
            		mediaType = "application/json",
            		schema = @Schema(implementation = ErrorResponseDto.class))})})
    @Operation(summary = "Get likes by commentary ID", description = "Returns all likes for a specific commentary")
    @GetMapping("/commentary/{id}")
    public ResponseEntity<List<LikeDto>> getLikesByCommentaryId(@PathVariable Long id) {
        return likeService.getLikesByCommentary(id);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Like deleted"),
        @ApiResponse(responseCode = "404", description = "Like not found",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @Operation(summary = "Delete a like by id", description = "Deletes the like with the given id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long id) {
        return likeService.deleteLike(id);
    }
}
