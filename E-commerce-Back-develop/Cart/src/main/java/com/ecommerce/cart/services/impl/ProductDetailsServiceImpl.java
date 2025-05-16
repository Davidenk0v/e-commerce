package com.ecommerce.cart.services.impl;

import com.ecommerce.cart.clients.ProductClient;
import com.ecommerce.cart.dto.request.ModelDto;
import com.ecommerce.cart.dto.response.ProductDetailsDto;
import com.ecommerce.cart.entities.Cart;
import com.ecommerce.cart.entities.ProductDetails;
import com.ecommerce.cart.mapper.IDtoMapper;
import com.ecommerce.cart.repositories.CartRepository;
import com.ecommerce.cart.repositories.ProductDetailsRepository;
import com.ecommerce.cart.services.ProductDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductDetailsServiceImpl implements ProductDetailsService {

    private final ProductDetailsRepository productDetailsRepository;
    private final CartRepository cartRepository;
    private final IDtoMapper<ProductDetails, ProductDetailsDto> dtoMapper;
    private final ProductClient productClient;

    @Override
    public ResponseEntity<ProductDetailsDto> getProductDetails(Long productId) {
        Optional<ProductDetails> productDetails = productDetailsRepository.findById(productId);
        if(productDetails.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Product with id %s not found", productId)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.entityToDto(productDetails.get()));
    }

    @Override
    public ResponseEntity<ProductDetailsDto> saveProductDetails(ProductDetailsDto productDetails) {
        try {
            ModelDto modelDto = productClient.getModel(productDetails.modelId());
            if(modelDto == null){
                throw new IllegalArgumentException("Model not found");
            }
            ProductDetails proDetails = productDetailsRepository.save(dtoMapper.dtoToEntity(productDetails));
            return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.entityToDto(proDetails));
        }catch (HttpClientErrorException.NotFound e){
            throw new IllegalArgumentException(
                    String.format("Model with id %s not found", productDetails.modelId())
            );
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IllegalArgumentException("Error saving product details");
        }
    }

    @Override
    public ResponseEntity<ProductDetailsDto> updateProductDetails(ProductDetailsDto productDetails) {
        Optional<ProductDetails> optionalProductDetails = productDetailsRepository.findById(productDetails.id());
        if(optionalProductDetails.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Product with id %s not found", productDetails.id())
            );
        }
        try {
            ProductDetails productDetailsToUpdate = optionalProductDetails.get();
            if(productDetails.quantity() != null){
            productDetailsToUpdate.setQuantity(productDetails.quantity());
            }
            if(productDetails.cartId() != null){
                Cart cart = cartRepository.findById(productDetails.cartId()).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
            productDetailsToUpdate.setCart(cart);
            }
            if(productDetails.modelId() != null){
            productDetailsToUpdate.setModelId(productDetails.modelId());
            }
            productDetailsRepository.save(productDetailsToUpdate);
            return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.entityToDto(productDetailsToUpdate));
        }catch (Exception e){
            throw new IllegalArgumentException("Error updating product details");
        }
    }

    @Override
    public ResponseEntity<ProductDetailsDto> deleteProductDetails(Long productId) {
        Optional<ProductDetails> optionalProductDetails = productDetailsRepository.findById(productId);
        if(optionalProductDetails.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Product with id %s not found", productId)
            );
        }
        try {
            productDetailsRepository.deleteById(productId);
            return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.entityToDto(optionalProductDetails.get()));
        }catch (Exception e){
            throw new IllegalArgumentException("Error deleting product details");
        }
    }

}
