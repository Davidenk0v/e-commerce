package com.ecommerce.payment.dto.request;

import lombok.Builder;

@Builder
public record BillCreationDto(
	String sessionId,
	Long orderId	
) {}
