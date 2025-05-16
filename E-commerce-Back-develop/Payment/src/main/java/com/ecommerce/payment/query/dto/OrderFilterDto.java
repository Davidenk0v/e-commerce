package com.ecommerce.payment.query.dto;

import com.ecommerce.payment.entity.OrderState;

import lombok.Builder;

@Builder
public record OrderFilterDto(
	String userId,
	OrderState state,
	Integer month,
	Integer year
) {}
