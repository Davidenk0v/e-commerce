package com.ecommerce.payment.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.payment.dto.request.BillCreationDto;
import com.ecommerce.payment.dto.request.StripeCheckoutRequest;
import com.ecommerce.payment.dto.request.StripeProductRequest;
import com.ecommerce.payment.dto.response.BillDto;
import com.ecommerce.payment.dto.response.StripeCheckoutResponse;
import com.ecommerce.payment.entity.Bill;
import com.ecommerce.payment.entity.Order;
import com.ecommerce.payment.entity.OrderState;
import com.ecommerce.payment.mapper.impl.BillMapper;
import com.ecommerce.payment.repository.BillRepository;
import com.ecommerce.payment.repository.OrderRepository;
import com.ecommerce.payment.service.IStripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Charge.PaymentMethodDetails;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerCreateParams.Address;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionRetrieveParams;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StripeService implements IStripeService {

	@Value("${stripe.secret}")
	private String stripeApiSecretKey;
	
	private final OrderRepository orderRepository;
	
	private final BillRepository billRepository;
	
	private final BillMapper billMapper;

	@Override
	public ResponseEntity<StripeCheckoutResponse> checkoutProducts(StripeCheckoutRequest stripeCheckoutRequest) throws StripeException {
		Stripe.apiKey = stripeApiSecretKey;
		
		Optional<Order> optOrder = orderRepository.findById(stripeCheckoutRequest.orderId());
		
		if (optOrder.isEmpty()) {
			throw new IllegalArgumentException(String.format("Pedido ref. '%s' no encontrado", stripeCheckoutRequest.orderId()));
		}
		
		List<LineItem> lineItems = new ArrayList<>();
		
		for (StripeProductRequest stripeProductRequest : stripeCheckoutRequest.stripeProductRequests()) {
			SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
					.builder().setName(stripeProductRequest.name()).build();
			
			SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
					.setCurrency(stripeProductRequest.currency())
					.setUnitAmount(stripeProductRequest.amount())
					.setProductData(productData).build();
			
			SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
					.setPriceData(priceData)
					.setQuantity(stripeProductRequest.quantity())
					.build();
			
			lineItems.add(lineItem);
		}
		
		CustomerCreateParams customerParams = CustomerCreateParams.builder()
			    .setEmail(stripeCheckoutRequest.email()) // assuming you have customer's email in your request
			    .setName(stripeCheckoutRequest.firstName() + " " + stripeCheckoutRequest.lastName())    // assuming you have customer's name
			    .setAddress(
			    	Address.builder()
			            .setLine1(stripeCheckoutRequest.street())
			            .setCity(stripeCheckoutRequest.locality())
			            .setState(stripeCheckoutRequest.province())
			            .setPostalCode(stripeCheckoutRequest.cp())
			            .setCountry("ES") // ISO country code: "US", "FR", etc.
			            .build()
			    )
			    .build();
		
		Customer customer = Customer.create(customerParams);
		
		SessionCreateParams params = SessionCreateParams.builder()
				.setMode(SessionCreateParams.Mode.PAYMENT)
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.addAllLineItem(lineItems)
				.setCustomer(customer.getId())
				.setSuccessUrl(
						String.format("http://localhost:5173/checkout-success/order/%s/cs/{CHECKOUT_SESSION_ID}", 
								stripeCheckoutRequest.orderId()))
				.setCancelUrl("http://localhost:5173/checkout-cancel")
				.build();
		
		Session session = null;
		
		
		session = Session.create(params);
		
		StripeCheckoutResponse stripeCheckoutResponse = StripeCheckoutResponse.builder()
				.status("SUCCES")
				.message("Checkout session created successfully")
				.sessionId(session.getId())
				.sessionUrl(session.getUrl())
				.build();
		
		return ResponseEntity
				.status(200)	
				.body(stripeCheckoutResponse);
	}
	
	@Override
	public ResponseEntity<BillDto> saveBillBySessionIdAndOrderId(BillCreationDto billCreationDto) throws StripeException {
	    Stripe.apiKey = stripeApiSecretKey;

	    String sessionId = billCreationDto.sessionId();
	    Long orderId = billCreationDto.orderId();
	    
	    // Check if the bill for the given order has already been created and returns it
	    Optional<Bill> existingBill = billRepository.findByOrderId(orderId);
		if (existingBill.isPresent()) {
			BillDto billDto = billMapper.toDto(existingBill.get());
			return ResponseEntity.status(200).body(billDto);
		}
	    
	    SessionRetrieveParams params = SessionRetrieveParams.builder()
	            .addExpand("payment_intent")
	            .addExpand("payment_intent.latest_charge")
	            .build();
	    
	    Session session = Session.retrieve(sessionId, params, null);
	    
	    PaymentIntent paymentIntent = session.getPaymentIntentObject();
	    Charge latestCharge = paymentIntent.getLatestChargeObject();

	    PaymentMethodDetails.Card cardDetails = null;
	    Charge.BillingDetails billingDetails = null;

	    if (latestCharge != null) {
	        if (latestCharge.getPaymentMethodDetails() != null) {
	            cardDetails = latestCharge.getPaymentMethodDetails().getCard();
	        }
	        billingDetails = latestCharge.getBillingDetails();
	    }

	    String billingName = billingDetails != null ? billingDetails.getName() : null;
	    String billingEmail = billingDetails != null ? billingDetails.getEmail() : null;

	    // Retrieve the customer to get saved address (from Customer object)
	    Customer customer = Customer.retrieve(session.getCustomer());

	    com.stripe.model.Address customerAddress = customer.getAddress();

	    String customerStreet = null;
	    String customerCity = null;
	    String customerProvince = null;
	    String customerPostalCode = null;
	    String customerCountry = null;

	    if (customerAddress != null) {
	        customerStreet = customerAddress.getLine1();
	        customerCity = customerAddress.getCity();
	        customerProvince = customerAddress.getState();
	        customerPostalCode = customerAddress.getPostalCode();
	        customerCountry = customerAddress.getCountry();
	    }
	    
	    Optional<Order> optOrder = orderRepository.findById(orderId);
	    
	    if (optOrder.isEmpty()) {
        	throw new IllegalArgumentException(String.format("Pedido ref. '%s' no encontrado", orderId));
        }
	    
	    Order order = optOrder.get();
	    
	    // Update state to CONFIRMED
	    order.setState(OrderState.CONFIRMED);
	    order = orderRepository.save(order);
	    
		Bill bill = Bill.builder()
				.name(billingName)
				.email(billingEmail)
				.cardBrand(cardDetails != null ? cardDetails.getBrand() : null)
				.cardNumber(cardDetails != null ? cardDetails.getLast4() : null)
				.cardExpMonth(cardDetails != null ? cardDetails.getExpMonth() : null)
				.cardExpYear(cardDetails != null ? cardDetails.getExpYear() : null)
				.amountReceived(paymentIntent.getAmountReceived())
				.currency(paymentIntent.getCurrency())
				.street(customerStreet)
				.locality(customerCity)
				.province(customerProvince)
				.cp(customerPostalCode)
				.country(customerCountry)
				.order(order)
				.build();
		
		bill = billRepository.save(bill);
		
		BillDto billDto = billMapper.toDto(bill);
		
		return ResponseEntity.status(201).body(billDto);
	}
}
