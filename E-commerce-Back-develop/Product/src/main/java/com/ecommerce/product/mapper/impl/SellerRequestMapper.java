package com.ecommerce.product.mapper.impl;

import com.ecommerce.product.clients.UserClient;
import com.ecommerce.product.dto.request.SellerRequestDto;
import com.ecommerce.product.dto.request.UserDto;
import com.ecommerce.product.dto.response.ModelDto;
import com.ecommerce.product.dto.response.ProductDto;
import com.ecommerce.product.dto.response.SellerRequestResponseDto;
import com.ecommerce.product.entity.Model;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.entity.SellerRequest;
import com.ecommerce.product.mapper.IDtoMapper;
import com.ecommerce.product.repository.ModelRepository;
import com.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Component
@RequiredArgsConstructor
public class SellerRequestMapper implements IDtoMapper<SellerRequestDto, SellerRequest, SellerRequestResponseDto> {

    private final UserClient userClient;
    private final ProductMapper productMapper;
    private final ModelMapper modelMapper;
    private final ModelRepository modelRepository;

    @Override
    public SellerRequest toEntity(SellerRequestDto dto) {
        return SellerRequest.builder()
                .id(dto.id())
                .userId(dto.userId())
                .productIds(dto.productIds())
                .documentURLs(dto.documentURLs())
                .status(dto.status())
                .build();
    }

    @Override
    public SellerRequestResponseDto toDto(SellerRequest entity) {
        try {
            UserDto userDto = userClient.getUserById(entity.getUserId());

            // Inicializar una lista vacía para manejar el caso donde productIds es null
            List<ModelDto> modelDtos = new ArrayList<>();

            // Verificar si productIds no es null antes de procesar
            if (entity.getProductIds() != null && !entity.getProductIds().isEmpty()) {
                List<Model> products = entity.getProductIds().stream()
                        .map(modelRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();

                // Registrar cuántos productos se encontraron vs. cuántos IDs había
                log.info("Found {}/{} products for seller request {}",
                        products.size(), entity.getProductIds().size(), entity.getId());

                modelDtos = products.stream().map(modelMapper::toDto).toList();
            } else {
                log.info("No product IDs found for seller request {}", entity.getId());
            }

            return SellerRequestResponseDto.builder()
                    .id(entity.getId())
                    .user(userDto)
                    .modelList(modelDtos)
                    .status(entity.getStatus())
                    .documentURLs(entity.getDocumentURLs())
                    .build();
        } catch (Exception e) {
            log.error("Error while mapping SellerRequest to SellerRequestResponseDto: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
