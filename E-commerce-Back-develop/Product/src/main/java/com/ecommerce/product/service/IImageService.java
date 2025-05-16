package com.ecommerce.product.service;

import com.ecommerce.product.dto.request.EOwnerType;

import io.minio.errors.MinioException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IImageService {

    ResponseEntity<String> uploadFile(MultipartFile file, Long ownerId, EOwnerType type) throws MinioException;

    ResponseEntity<byte[]> downloadFile(String fileName) throws MinioException;

    ResponseEntity<byte[]> getImage(String imageName);
}
