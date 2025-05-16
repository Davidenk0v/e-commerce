package com.ecommerce.payment.mapper.impl;

import org.springframework.stereotype.Component;

import com.ecommerce.payment.dto.request.AddressCreationDto;
import com.ecommerce.payment.dto.response.AddressDto;
import com.ecommerce.payment.entity.Address;
import com.ecommerce.payment.mapper.IDtoMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddressMapper implements IDtoMapper<AddressCreationDto, Address, AddressDto> {
	
	@Override
	public Address toEntity(AddressCreationDto dto) {
		return Address.builder()
				.userId(dto.userId())
				.locality(dto.locality())
				.province(dto.province())
				.street(dto.street())
				.addressAlias(dto.addressAlias())
				.cp(dto.cp())
				.build();
	}

	@Override
	public AddressDto toDto(Address entity) {
		return AddressDto.builder()
				.id(entity.getId())
				.userId(entity.getUserId())
				.locality(entity.getLocality())
				.province(entity.getProvince())
				.street(entity.getStreet())
				.addressAlias(entity.getAddressAlias())
				.cp(entity.getCp())
				.build();
	}
	
}
