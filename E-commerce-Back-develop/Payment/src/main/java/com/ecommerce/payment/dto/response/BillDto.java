package com.ecommerce.payment.dto.response;

import lombok.Builder;

@Builder
public record BillDto(
	Long id,
	String name,
	String email,
	String cardBrand,
	String cardNumber,
	Long cardExpMonth,
	Long cardExpYear,
	Long amountReceived,
	String currency,
	String creationDate,
	String street,
	String locality,
	String province,
	String cp,
	String country,
	OrderDto orderDto
) {}
