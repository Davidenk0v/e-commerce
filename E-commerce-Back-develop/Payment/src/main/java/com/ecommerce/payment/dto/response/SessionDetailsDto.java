package com.ecommerce.payment.dto.response;

import lombok.Builder;

@Builder
public record SessionDetailsDto(	
	String sessionId,
	String paymentIntentId,
	String paymentStatus,
	String cardBrand,
	String cardLast4,
	Long cardExpMonth,
	Long cardExpYear,
	Long amountReceived,
	String currency,
    String billingName,
    String billingEmail,
    String billingStreet,
    String billingLocality,
    String billingProvince,
    String billingCP,
    String billingCountry
) {}
