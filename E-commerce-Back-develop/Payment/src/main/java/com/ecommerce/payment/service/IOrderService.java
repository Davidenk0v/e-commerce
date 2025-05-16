package com.ecommerce.payment.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.ecommerce.payment.dto.request.OrderCreationDto;
import com.ecommerce.payment.dto.response.OrderDto;
import com.ecommerce.payment.entity.OrderState;

public interface IOrderService {

	ResponseEntity<OrderDto> saveOrder(OrderCreationDto orderCreationDto);

	ResponseEntity<List<OrderDto>> getOrdersByUserId(String userId);

	ResponseEntity<OrderDto> cancelOrder(Long orderId);

	ResponseEntity<OrderDto> confirmOrder(Long orderId);

	ResponseEntity<Page<OrderDto>> getFilteredOrders(
			String userId,
			OrderState state, 
			Integer month, 
			Integer year, 
			Integer page,
			Integer size, 
			String sort);

	ResponseEntity<OrderDto> getOrderById(Long orderId);
}
