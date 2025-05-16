package com.ecommerce.payment.mapper.impl;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.ecommerce.payment.dto.request.OrderCreationDto;
import com.ecommerce.payment.dto.response.BillDto;
import com.ecommerce.payment.dto.response.OrderDto;
import com.ecommerce.payment.entity.Bill;
import com.ecommerce.payment.entity.Order;
import com.ecommerce.payment.mapper.IDtoMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BillMapper {
	
	private static final String TIME_PATTERN = "dd/MM/yyyy";
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN).withZone(ZoneId.systemDefault());

	private final  IDtoMapper<OrderCreationDto, Order, OrderDto> orderMapper;
	
	public BillDto toDto(Bill entity) {
		
		Order order = entity.getOrder();
		OrderDto orderDto = orderMapper.toDto(order);
		
		return BillDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.email(entity.getEmail())
				.cardBrand(entity.getCardBrand())
				.cardNumber(entity.getCardNumber())
				.cardExpMonth(entity.getCardExpMonth())
				.cardExpYear(entity.getCardExpYear())
				.amountReceived(entity.getAmountReceived())
				.currency(entity.getCurrency())
				.creationDate(formatter.format(entity.getCreationDate()))
				.street(entity.getStreet())
				.locality(entity.getLocality())
				.province(entity.getProvince())
				.cp(entity.getCp())
				.country(entity.getCountry())
				.orderDto(orderDto)
				.build();
	}
}
