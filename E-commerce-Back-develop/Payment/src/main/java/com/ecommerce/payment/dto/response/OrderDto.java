package com.ecommerce.payment.dto.response;

import java.util.List;

import com.ecommerce.payment.entity.OrderState;

import lombok.Builder;

@Builder
public record OrderDto(
	Long id, 
	String userId, 
	String creationDate, 
	String updateDate, 
	OrderState state,
	List<OrderDetailDto> orderDetails
) {}
