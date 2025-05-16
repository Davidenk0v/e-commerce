package com.ecommerce.payment.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.payment.dto.request.OrderCreationDto;
import com.ecommerce.payment.dto.response.ErrorResponseDto;
import com.ecommerce.payment.dto.response.OrderDto;
import com.ecommerce.payment.entity.OrderState;
import com.ecommerce.payment.service.IOrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment/order")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Operations related to orders")
public class OrderController {

	private final IOrderService orderService;
	
	@ApiResponses(value = {
        @ApiResponse(
        	responseCode = "201", 
			description="Order created successfully"),
        @ApiResponse(
        	responseCode = "400", 
        	description = "Invalid request body",
            content = {
            	@Content(
            		examples = {@ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"Order must have at least one article on it\",\"details\":\"uri=/api/v1/payment/order\"}")},
            		mediaType = "application/json",
            		schema = @Schema(implementation = ErrorResponseDto.class))})})
	    @Operation(
	    	summary = "Create a new order",
	    	description = "Creates an order after checking that the model and the warehouse provided exist and that there is no stock already registered for the given model at the given warehouse")
	@PostMapping
	public ResponseEntity<OrderDto> saveOrder(@RequestBody OrderCreationDto orderCreationDto) {
		return orderService.saveOrder(orderCreationDto);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Orders found", 
			content = {@Content(examples = {
				@ExampleObject(value = "[{\"id\":1,\"userId\":\"569f67de-36e6-4552-ac54-e52085109818\",\"creationDate\":\"03/04/2025\",\"updateDate\":\"03/04/2025\",\"state\":\"PENDING\",\"orderDetails\":[{\"id\":1,\"modelId\":\"1\",\"quantity\":5,\"orderId\":3},{\"id\":2,\"modelId\":\"2\",\"quantity\":3,\"orderId\":1}]")})})})
		@Operation(
			summary = "Get all orders for the given user", 
			description = "Returns all orders for the given user, if no orders are found, returns an empty list")
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<OrderDto>> getOrdersByUserId(@PathVariable String userId) {
		return orderService.getOrdersByUserId(userId);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Order cancelled"),
		@ApiResponse(
			responseCode = "404", 
			description = "Order not found",
			content = {
				@Content(schema = @Schema(implementation = Void.class))})})
	@Operation(
		summary = "Sets the state of an order to CANCELLED", 
		description = "If the given id results in a found order, sets its state to CANCELLED, if no order is found, returns NOT FOUND")
	@PatchMapping("/{orderId}/cancel")
	public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long orderId) {
		return orderService.cancelOrder(orderId);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Order confirmed"),
		@ApiResponse(
			responseCode = "404", 
			description = "Order not found",
			content = {
				@Content(schema = @Schema(implementation = Void.class))})})
	@Operation(
		summary = "Sets the state of an order to CONFIRMED", 
		description = "If the given id results in a found order, sets its state to CONFIRMED, if no order is found, returns NOT FOUND")
	@PatchMapping("/{orderId}/confirm")
	public ResponseEntity<OrderDto> confirmOrder(@PathVariable Long orderId) {
		return orderService.confirmOrder(orderId);
	}
	
	@GetMapping("/search")
	public ResponseEntity<Page<OrderDto>> getFilteredOrders(
		@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "5") Integer size,
        @RequestParam(defaultValue = "[{\"field\":\"creationDate\",\"direction\":\"desc\"}]") String sort,
        @RequestParam(required = false) String userId,
        @RequestParam(required = false) OrderState state,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
	) {
		return orderService.getFilteredOrders(userId, state, month, year, page, size, sort);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
		return orderService.getOrderById(orderId);
	}
}
