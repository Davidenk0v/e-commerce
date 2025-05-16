package com.ecommerce.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.product.dto.response.ModelStateSummaryDto;
import com.ecommerce.product.service.IModelService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product/seller-statistics")
@RequiredArgsConstructor
@Tag(name = "Seller statistics", description = "Operations related to seller statistics")
public class SellerController {

	private final IModelService modelService;
	
	@GetMapping("/model-state/seller/{sellerId}")
	public ResponseEntity<ModelStateSummaryDto> getModelStateSummary(@PathVariable String sellerId) {
		return modelService.getModelStateSummary(sellerId);
	}
}
