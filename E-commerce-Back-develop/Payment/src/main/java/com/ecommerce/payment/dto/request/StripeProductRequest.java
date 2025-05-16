package com.ecommerce.payment.dto.request;

import lombok.Builder;

@Builder
public record StripeProductRequest(
	String name, 
	Long quantity, 
	String currency,
	Long amount
) {}
