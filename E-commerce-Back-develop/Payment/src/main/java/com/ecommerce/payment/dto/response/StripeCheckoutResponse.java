package com.ecommerce.payment.dto.response;

import lombok.Builder;

@Builder
public record StripeCheckoutResponse(
	String status,
	String message,
	String sessionId, 
	String sessionUrl
) {}
