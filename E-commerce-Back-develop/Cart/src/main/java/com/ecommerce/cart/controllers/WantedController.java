package com.ecommerce.cart.controllers;

import com.ecommerce.cart.dto.response.ProductDetailsDto;
import com.ecommerce.cart.dto.response.WantedDto;
import com.ecommerce.cart.services.WantedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart/wanted")
@RequiredArgsConstructor
public class WantedController {

    private final WantedService wantedService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<WantedDto>> getWantedList(@PathVariable String userId) {
        return wantedService.getWantedList(userId);
    }

    @PostMapping("/{userId}/{modelId}")
    public ResponseEntity<WantedDto> addToWanted(@PathVariable String userId, @PathVariable Long modelId) {
        return wantedService.addToWantedList(modelId, userId);
    }

    @DeleteMapping("/{userId}/{modelId}")
    public ResponseEntity<WantedDto> deleteFromWanted(@PathVariable String userId, @PathVariable Long modelId) {
        return wantedService.removeFromWantedList(modelId, userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteWanted(@PathVariable String userId) {
        return wantedService.clearWantedList(userId);
    }
}
