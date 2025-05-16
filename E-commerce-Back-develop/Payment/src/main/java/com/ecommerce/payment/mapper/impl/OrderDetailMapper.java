package com.ecommerce.payment.mapper.impl;

import org.springframework.stereotype.Component;

import com.ecommerce.payment.dto.request.OrderDetailCreationDto;
import com.ecommerce.payment.dto.response.OrderDetailDto;
import com.ecommerce.payment.entity.Order;
import com.ecommerce.payment.entity.OrderDetail;
import com.ecommerce.payment.mapper.IDtoMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderDetailMapper implements IDtoMapper<OrderDetailCreationDto, OrderDetail, OrderDetailDto> {
	
	public OrderDetail toEntity(OrderDetailCreationDto dto, Order order) {
		return OrderDetail.builder()
				.order(order)
				.modelId(dto.modelId())
				.name(dto.name())
				.price(dto.price())
				.quantity(dto.quantity())
				.build();
	}

	@Override
	public OrderDetailDto toDto(OrderDetail entity) {
		return OrderDetailDto.builder()
				.id(entity.getId())
				.modelId(entity.getModelId())
				.name(entity.getName())
				.price(entity.getPrice())
				.quantity(entity.getQuantity())
				.orderId(entity.getOrder().getId())
				.build();
	}

	@Override
	public OrderDetail toEntity(OrderDetailCreationDto dto) {
		return OrderDetail.builder()
				.modelId(dto.modelId())
				.name(dto.name())
				.price(dto.price())	
				.quantity(dto.quantity())
				.build();
	}
	
}