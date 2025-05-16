package com.ecommerce.payment.service;

import org.springframework.http.ResponseEntity;

import com.ecommerce.payment.dto.request.BillCreationDto;
import com.ecommerce.payment.dto.request.StripeCheckoutRequest;
import com.ecommerce.payment.dto.response.BillDto;
import com.ecommerce.payment.dto.response.StripeCheckoutResponse;
import com.stripe.exception.StripeException;

public interface IStripeService {

	// stripe API expects the following parameters:
	// name, amount, quantity, currency
	// and returns:
	// sessionId, sessionUrl
	public ResponseEntity<StripeCheckoutResponse> checkoutProducts(StripeCheckoutRequest stripeCheckoutRequest) throws StripeException;
	
	public ResponseEntity<BillDto> saveBillBySessionIdAndOrderId(BillCreationDto billCreationDto) throws StripeException;
}
