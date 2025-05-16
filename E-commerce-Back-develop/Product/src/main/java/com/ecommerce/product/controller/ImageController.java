package com.ecommerce.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.product.dto.request.EOwnerType;
import com.ecommerce.product.dto.response.ErrorResponseDto;
import com.ecommerce.product.service.IImageService;

import io.minio.errors.MinioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/product/image")
@Tag(name = "Image", description = "Operations related to image upload and download")
public class ImageController {


	private final IImageService imageService;

    public ImageController(IImageService imageService) {
        this.imageService = imageService;
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Image uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body",
                content = {@Content(schema = @Schema(implementation = ErrorResponseDto.class))}),
        @ApiResponse(responseCode = "500", description = "Error uploading image",
                content = {@Content(schema = @Schema(implementation = ErrorResponseDto.class))})
    })
    @Operation(
        summary = "Upload an image",
        description = "Uploads an image and associates it with an owner (MODEL or COMMENT)"
    )
    @PostMapping("/upload/{ownerId}/{type}")
    public ResponseEntity<String> postImage(@RequestParam MultipartFile file, @PathVariable Long ownerId, @PathVariable EOwnerType type) throws MinioException {
        return imageService.uploadFile(file, ownerId, type);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Image downloaded successfully"),
        @ApiResponse(
    		responseCode = "404", description = "Image not found",
            content = {@Content(schema = @Schema(implementation = ErrorResponseDto.class))}),
        @ApiResponse(
    		responseCode = "500", description = "Error downloading image",
            content = {@Content(schema = @Schema(implementation = ErrorResponseDto.class))})
    })
    @Operation(
        summary = "Download an image",
        description = "Retrieves an image from storage by its filename"
    )
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadImage(@RequestParam String fileName) throws MinioException {
        return imageService.downloadFile(fileName);
    }

    @GetMapping("/get-image")
    public ResponseEntity<byte[]> getImage(@RequestParam String fileName) {
        return imageService.getImage(fileName);
    }
}