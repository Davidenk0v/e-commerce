package com.ecommerce.payment.mapper.impl;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ecommerce.payment.dto.request.OrderCreationDto;
import com.ecommerce.payment.dto.response.OrderDetailDto;
import com.ecommerce.payment.dto.response.OrderDto;
import com.ecommerce.payment.entity.Order;
import com.ecommerce.payment.entity.OrderDetail;
import com.ecommerce.payment.mapper.IDtoMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper implements IDtoMapper<OrderCreationDto, Order, OrderDto> {
	
	private static final String TIME_PATTERN = "dd/MM/yyyy";
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN).withZone(ZoneId.systemDefault());
	
	private final OrderDetailMapper orderDetailMapper;
	
	@Override
	public Order toEntity(OrderCreationDto dto) {
		return Order.builder()
				.userId(dto.userId())
				.build();
	}

	@Override
	public OrderDto toDto(Order entity) {
		List<OrderDetailDto> orderDetailDtos = entity.getOrderDetails()
				.stream()
				.map(orderDetailMapper::toDto)
				.toList();
		
		return OrderDto.builder()
				.id(entity.getId())
				.userId(entity.getUserId())
				.creationDate(formatter.format(entity.getCreationDate()))
				.updateDate(formatter.format(entity.getUpdateDate()))
				.state(entity.getState())
				.orderDetails(orderDetailDtos)
				.build();
	}
	
	public OrderDto toDto(Order entity, List<OrderDetail> orderDetails) {
		List<OrderDetailDto> orderDetailDtos = orderDetails
				.stream()
				.map(orderDetailMapper::toDto)
				.toList();
		
		return OrderDto.builder()
				.id(entity.getId())
				.userId(entity.getUserId())
				.creationDate(formatter.format(entity.getCreationDate()))
				.updateDate(formatter.format(entity.getUpdateDate()))
				.state(entity.getState())
				.orderDetails(orderDetailDtos)
				.build();
	}


}
