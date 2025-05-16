package com.ecommerce.payment.dto.request;

import java.util.List;

import lombok.Builder;

@Builder
public record OrderCreationDto(
	String userId,
	List<OrderDetailCreationDto> orderDetails
) {}
