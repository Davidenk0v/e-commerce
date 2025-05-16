package com.ecommerce.product.service;

import com.ecommerce.product.dto.request.SellerRequestDto;
import com.ecommerce.product.dto.response.SellerRequestResponseDto;
import com.ecommerce.product.entity.ERequestState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISellerRequestService {
    ResponseEntity<List<SellerRequestResponseDto>> getAllSellerRequestsByUserId(String userId);

    ResponseEntity<List<SellerRequestResponseDto>> getAllSellerRequests();

    ResponseEntity<SellerRequestResponseDto> getSellerRequestById(Long requestId);

    ResponseEntity<SellerRequestResponseDto> changeRequestStatus(Long requestId, ERequestState status);

    ResponseEntity<String> createSellerRequest(SellerRequestDto sellerDto, List<MultipartFile> documentation);
}
