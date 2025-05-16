package com.ecommerce.payment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.payment.dto.response.BestSellerProductDto;
import com.ecommerce.payment.dto.response.SalesResultDto;
import com.ecommerce.payment.service.ISellerService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment/seller")
@RequiredArgsConstructor
@Tag(name = "Seller", description = "Operations related to selling statistics")
public class SellerController {
	
	private final ISellerService sellerService;

	@GetMapping("/{sellerId}/best-selling")
	public ResponseEntity<List<BestSellerProductDto>> getBestSellingProducts(
		@RequestParam(defaultValue = "5") Integer limit,
        @PathVariable String sellerId
	) {
		return sellerService.getBestSellingProducts(limit, sellerId);
	}
	
	@GetMapping("/{sellerId}/sales-result")
	public ResponseEntity<SalesResultDto> getSalesResult(@PathVariable String sellerId) {
		return sellerService.getSalesResult(sellerId);
	}
}
