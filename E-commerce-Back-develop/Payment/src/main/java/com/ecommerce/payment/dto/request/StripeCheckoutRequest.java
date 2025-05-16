package com.ecommerce.payment.dto.request;

import java.util.List;

import lombok.Builder;

@Builder
public record StripeCheckoutRequest(
	Long orderId,
	List<StripeProductRequest> stripeProductRequests,
	String firstName,
	String lastName,
	String email,
	String street,
	String locality,
	String province,
	String cp	
) {}
