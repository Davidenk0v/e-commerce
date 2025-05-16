package com.ecommerce.cart.services.impl;

import com.ecommerce.cart.dto.response.CartDto;
import com.ecommerce.cart.dto.response.ProductDetailsDto;
import com.ecommerce.cart.entities.Cart;
import com.ecommerce.cart.entities.ProductDetails;
import com.ecommerce.cart.mapper.IDtoMapper;
import com.ecommerce.cart.repositories.CartRepository;
import com.ecommerce.cart.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final IDtoMapper<Cart, CartDto> dtoMapper;
    private final IDtoMapper<ProductDetails, ProductDetailsDto> productDtoMapper;

    // Add item to cart
    @Override
    public ResponseEntity<CartDto> addModelToCart(String userId, ProductDetailsDto productDetails) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if(optionalCart.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("No se encontró carro para el usuario", productDetails.cartId())
            );
        }
        Cart cart = optionalCart.get();
        boolean productExists =  false;
        // Verificamos si el producto ya existe en el carrito
        for (ProductDetails product : cart.getProductsDetails()) {
            if (product.getModelId().equals(productDetails.modelId())) {
                product.setQuantity(product.getQuantity() + productDetails.quantity());
                productExists = true;
                break;
            }
        }
        // Si el producto no existe, lo agregamos al carro
        if (!productExists) {
            ProductDetails newProduct = productDtoMapper.dtoToEntity(productDetails);
            cart.getProductsDetails().add(newProduct);
        }
        // Guardamos el carrito actualizado
        cartRepository.save(cart);
        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.entityToDto(cart));
    }

    // Update quantity item to cart
    @Override
    public ResponseEntity<CartDto> updateModelQuantity(Long modelId, String userId, Integer quantity) {
        // Buscar el carrito por su ID
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Cart with id %s not found", userId)
                ));
        try {
            AtomicBoolean productFound = new AtomicBoolean(false);

            // Obtener la lista actual de productos
            List<ProductDetails> currentProducts = cart.getProductsDetails();

            // Crear una nueva lista con los productos actualizados
            List<ProductDetails> updatedProducts = currentProducts.stream()
                    .peek(product -> {
                        // Si encontramos el producto que queremos eliminar
                        if (product.getId().equals(modelId)) {
                            productFound.set(true);
                            // Decrementar la cantidad
                            product.setQuantity(quantity);
                        }
                        // Filtrar los productos con cantidad mayor a 0
                    }).filter( product -> product.getQuantity() > 0).toList();

            // Si no se encontró el producto, lanzar excepción
            if (!productFound.get()) {
                throw new IllegalArgumentException(
                        String.format("Product with id %s not found in cart", modelId)
                );
            }
            // Actualizar la lista de productos del carrito
            cart.setProductsDetails(updatedProducts);

            // Guardar el carrito actualizado
            Cart updatedCart = cartRepository.save(cart);

            // Convertir a DTO y devolver respuesta
            return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.entityToDto(updatedCart));

        } catch (Exception e) {
            throw new IllegalArgumentException("Error removing product from cart: " + e.getMessage());
        }
    }


    // Clear all quantity of a model from cart
    @Override
    public ResponseEntity<CartDto> clearModelFromCart(Long modelId, Long cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if(optionalCart.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Cart with id %s not found", cartId)
            );
        }
        try {
            Cart cart = optionalCart.get();
            cart.getProductsDetails().removeIf(productDetails -> productDetails.getId().equals(modelId));
            cartRepository.save(cart);
            return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.entityToDto(cart));
        }catch (Exception e){
            throw new IllegalArgumentException("Error clearing product from cart");
        }
    }

    // Clear all items from cart
    @Override
    public ResponseEntity<CartDto> clearCart(Long cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if(optionalCart.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Cart with id %s not found", cartId)
            );
        }
        try {
            Cart cart = optionalCart.get();
            cart.getProductsDetails().clear();
            cartRepository.save(cart);
            return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.entityToDto(cart));
        }catch (Exception e){
            throw new IllegalArgumentException("Error clearing cart");
        }
    }

    @Override
    public ResponseEntity<CartDto> getUserCart(String userId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if(optionalCart.isEmpty()){
            throw new IllegalArgumentException("No se encontró carrito para el usuario. Por favor, si debería tenerlo, notifíquelo lo antes posible.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.entityToDto(optionalCart.get()));
    }

    @Override
    public ResponseEntity<CartDto> newUserCart(String userId) {
        Cart cart = Cart.builder()
                .userId(userId)
                .productsDetails(List.of())
                .build();
        cartRepository.save(cart);
        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.entityToDto(cart));
    }
}
