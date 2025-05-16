package com.ecommerce.cart.controllers;

import com.ecommerce.cart.dto.response.CartDto;
import com.ecommerce.cart.dto.error.ErrorResponseDto;
import com.ecommerce.cart.dto.response.ProductDetailsDto;
import com.ecommerce.cart.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Operations related to user carts")
public class CartController {

    private final CartService cartService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = {@Content(schema = @Schema(implementation = ErrorResponseDto.class))})
    })
    @Operation(summary = "Get user cart", description = "Retrieves the cart associated with the given user ID")
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId) {
        return cartService.getUserCart(userId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added to cart successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product details",
                    content = {@Content(schema = @Schema(implementation = ErrorResponseDto.class))})
    })
    @Operation(summary = "Add product to cart", description = "Adds a product to the user's cart")
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addToCart(@PathVariable String userId, @RequestBody ProductDetailsDto productDetails) {
        return cartService.addModelToCart(userId, productDetails);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart updated successfully"),
            @ApiResponse(responseCode = "404", description = "Cart or product not found",
                    content = {@Content(schema = @Schema(implementation = ErrorResponseDto.class))})
    })
    @Operation(summary = "Update cart quantity", description = "Updates the quantity of a specific product in the user's cart")
    @PutMapping("/{userId}/{quantity}/{modelId}")
    public ResponseEntity<CartDto> updateCart(@PathVariable String userId, @PathVariable Integer quantity, @PathVariable Long modelId) {
        return cartService.updateModelQuantity(modelId, userId, quantity);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = {@Content(schema = @Schema(implementation = ErrorResponseDto.class))})
    })
    @Operation(summary = "Clear cart", description = "Removes all items from the cart with the given ID")
    @DeleteMapping("/{cartId}")
    public ResponseEntity<CartDto> deleteCart(@PathVariable Long cartId) {
        return cartService.clearCart(cartId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New cart created successfully"),
            @ApiResponse(responseCode = "400", description = "Cart creation failed",
                    content = {@Content(schema = @Schema(implementation = ErrorResponseDto.class))})
    })
    @Operation(summary = "Create new cart", description = "Creates a new cart for the specified user")
    @PostMapping("/new-cart/{userId}")
    public ResponseEntity<CartDto> createCart(@PathVariable String userId) {
        return cartService.newUserCart(userId);
    }
}
