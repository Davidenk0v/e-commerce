package com.ecommerce.payment.dto.response;

import lombok.Builder;

@Builder
public record ErrorResponseDto(
	// Examples are not provided here since they vary from case to case. 
	// They are provided at the controller level instead.
	String status,
	String message, 
	String details
) {}
