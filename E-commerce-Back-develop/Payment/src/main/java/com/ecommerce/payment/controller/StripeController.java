package com.ecommerce.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.payment.dto.request.BillCreationDto;
import com.ecommerce.payment.dto.request.StripeCheckoutRequest;
import com.ecommerce.payment.dto.response.BillDto;
import com.ecommerce.payment.dto.response.StripeCheckoutResponse;
import com.ecommerce.payment.service.IStripeService;
import com.stripe.exception.StripeException;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment/stripe")
@RequiredArgsConstructor
@Tag(name = "Stripe", description = "Operations related to Stripe payments")
public class StripeController {

	private final IStripeService stripeService;
	
	@PostMapping("/checkout")
	public ResponseEntity<StripeCheckoutResponse> checkoutProducts(@RequestBody StripeCheckoutRequest stripeCheckoutRequest) throws StripeException {
        return stripeService.checkoutProducts(stripeCheckoutRequest);
	}
	
	@PostMapping("/create-bill")
	public ResponseEntity<BillDto> saveBillBySessionIdAndOrderId(@RequestBody BillCreationDto billCreationDto) throws StripeException {
        return stripeService.saveBillBySessionIdAndOrderId(billCreationDto);
	}
}
