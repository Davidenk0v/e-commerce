package com.ecommerce.product.service.impl;

import java.io.InputStream;
import java.util.ArrayList;

import com.ecommerce.product.repository.SellerRequestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.product.dto.request.EOwnerType;
import com.ecommerce.product.entity.Image;
import com.ecommerce.product.repository.CommentaryRepository;
import com.ecommerce.product.repository.ImageRepository;
import com.ecommerce.product.repository.ModelRepository;
import com.ecommerce.product.service.IImageService;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import io.restassured.internal.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {


    private final MinioClient minioClient;

    private final ImageRepository imageRepository;

    private final ModelRepository modelRepository;
    
    private final CommentaryRepository commentaryRepository;

    private final SellerRequestRepository sellerRequestRepository;


    @Value("${minio.bucket.name}")
    private String bucketName;

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile file, Long ownerId, EOwnerType type) throws MinioException {
        try {
        	// Get image data from request
            String fileName = file.getOriginalFilename();
            String hashName = generateHash(fileName);
            InputStream inputStream = file.getInputStream();
            
            // Check if bucket exists, create if not
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(hashName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(file.getContentType())
                    .build();
            
            // Upload image to MinIO
            minioClient.putObject(putObjectArgs);
            
            Image image = new Image(null, hashName);
            imageRepository.save(image);
            if(type == EOwnerType.MODEL){
                modelRepository.findById(ownerId).ifPresent(model -> {
                    model.getImages().add(image);
                    modelRepository.save(model);
                });
            } else if(type == EOwnerType.COMMENTARY) {
				commentaryRepository.findById(ownerId).ifPresent(commentary -> {
					commentary.getImages().add(image);
					commentaryRepository.save(commentary);
				});
            } else if(type == EOwnerType.SELLER_REQUEST) {
                sellerRequestRepository.findById(ownerId).ifPresent(request -> {
                    if(request.getDocumentURLs() == null){
                        request.setDocumentURLs(new ArrayList<>());
                    }
                    request.getDocumentURLs().add(image.getUrl());
                    sellerRequestRepository.save(request);
                });
            }

            return ResponseEntity.status(HttpStatus.OK).body(image.getUrl());

        }catch (Exception e){
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(String fileName) throws MinioException {
        try {
            InputStream response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build());
            byte[] content = response.readAllBytes();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(content);
        } catch (Exception e) {
        	throw new InternalError(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<byte[]> getImage(String imageName) {
        MediaType contentType = determineContentType(imageName);
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket("products")
                            .object(imageName)
                            .build()
            );

            byte[] bytes = IOUtils.toByteArray(stream);
            stream.close();
            return ResponseEntity
                    .ok()
                    .contentType(contentType) // Ajusta según el tipo de imagen
                    .body(bytes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

	public void deleteImage(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error deleting image: {}", e.getMessage());
        }
    }

    private MediaType determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            case "gif" -> MediaType.IMAGE_GIF;
            // Añade más tipos según sea necesario
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }

    static String generateHash(String fileName) {
        return System.currentTimeMillis() + "_" + fileName;
    }
}
