package com.ecommerce.payment.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ecommerce.payment.dto.response.BestSellerProductDto;
import com.ecommerce.payment.dto.response.SalesResultDto;

public interface ISellerService {

	ResponseEntity<List<BestSellerProductDto>> getBestSellingProducts(Integer limit, String sellerId);
	
	ResponseEntity<SalesResultDto> getSalesResult(String sellerId);
}
