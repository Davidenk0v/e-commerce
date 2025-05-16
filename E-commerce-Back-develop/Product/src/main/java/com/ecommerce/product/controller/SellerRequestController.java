package com.ecommerce.product.controller;

import com.ecommerce.product.dto.request.SellerRequestDto;
import com.ecommerce.product.dto.response.SellerRequestResponseDto;
import com.ecommerce.product.entity.ERequestState;
import com.ecommerce.product.service.ISellerRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product/seller-request")
@RequiredArgsConstructor
@Tag(name = "Seller Request", description = "Operations related to seller requests")
public class SellerRequestController {

    private final ISellerRequestService sellerRequestService;

    @GetMapping
    public ResponseEntity<List<SellerRequestResponseDto>> getAllSellerRequests() {
        return sellerRequestService.getAllSellerRequests();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SellerRequestResponseDto>> getAllSellerRequestsByUserId(@PathVariable String userId) {
        return sellerRequestService.getAllSellerRequestsByUserId(userId);
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<SellerRequestResponseDto> getSellerRequestById(@PathVariable Long requestId) {
        return sellerRequestService.getSellerRequestById(requestId);
    }

    @PostMapping(value = "/sellers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createSellerRequest(
            @RequestPart("seller") SellerRequestDto sellerDto,
            @RequestPart(value = "documents", required = false) List<MultipartFile> documents) {
        return sellerRequestService.createSellerRequest(sellerDto, documents);
    }

    @PutMapping("/request-status/{requestId}/{state}")
    public ResponseEntity<SellerRequestResponseDto> createSellerRequest(
            @PathVariable Long requestId,
            @PathVariable ERequestState state) {
        return sellerRequestService.changeRequestStatus(requestId, state);
    }
}
