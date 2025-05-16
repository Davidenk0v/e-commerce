package com.ecommerce.product.service.impl;

import com.ecommerce.product.dto.request.EOwnerType;
import com.ecommerce.product.dto.request.SellerRequestDto;
import com.ecommerce.product.dto.response.SellerRequestResponseDto;
import com.ecommerce.product.entity.ERequestState;
import com.ecommerce.product.entity.SellerRequest;
import com.ecommerce.product.mapper.impl.SellerRequestMapper;
import com.ecommerce.product.repository.SellerRequestRepository;
import com.ecommerce.product.service.ISellerRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerRequestService implements ISellerRequestService {

    private final SellerRequestRepository sellerRequestRepository;

    private final SellerRequestMapper sellerRequestMapper;

    private final ImageService imageService;

    @Override
    public ResponseEntity<List<SellerRequestResponseDto>> getAllSellerRequestsByUserId(String userId) {
        List<SellerRequestResponseDto> requestList = sellerRequestRepository.findAllByUserId(userId)
                .stream()
                .map(sellerRequestMapper::toDto)
                .toList();

        return ResponseEntity.ok(requestList);
    }


    @Override
    public ResponseEntity<List<SellerRequestResponseDto>> getAllSellerRequests() {
        List<SellerRequestResponseDto> requestList = sellerRequestRepository.findAll()
                .stream()
                .map(sellerRequestMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(requestList);
    }

    @Override
    public ResponseEntity<SellerRequestResponseDto> getSellerRequestById(Long requestId) {
        Optional<SellerRequest> request = sellerRequestRepository.findById(requestId);
        if(request.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(request.map(sellerRequestMapper::toDto).orElse(null));
    }

    @Override
    public ResponseEntity<SellerRequestResponseDto> changeRequestStatus(Long requestId, ERequestState status) {
        Optional<SellerRequest> request = sellerRequestRepository.findById(requestId);
        if(request.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        SellerRequest sellerRequest = request.get();
        sellerRequest.setStatus(status);
        sellerRequestRepository.save(sellerRequest);

        return ResponseEntity.ok(sellerRequestMapper.toDto(sellerRequest));
    }

    @Override
    public ResponseEntity<String> createSellerRequest(SellerRequestDto sellerDto, List<MultipartFile> documentation) {
        if(sellerDto == null) {
            return ResponseEntity.badRequest().body("Seller request cannot be null");
        }
        try {
            SellerRequest sellerRequest = sellerRequestMapper.toEntity(sellerDto);
            sellerRequest.setStatus(ERequestState.PENDING);
            sellerRequest = sellerRequestRepository.save(sellerRequest);
            if (documentation != null) {
                for (MultipartFile file : documentation) {
                    String url = imageService.uploadFile(file, sellerRequest.getId(), EOwnerType.SELLER_REQUEST).getBody();
                }
            }
        }catch (Exception e){
            throw new RuntimeException("Error while creating seller request: " + e.getMessage());
        }
        return ResponseEntity.ok("Seller request created successfully");
    }
}
