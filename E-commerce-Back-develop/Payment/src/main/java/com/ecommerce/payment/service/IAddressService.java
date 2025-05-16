package com.ecommerce.payment.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ecommerce.payment.dto.request.AddressCreationDto;
import com.ecommerce.payment.dto.response.AddressDto;

public interface IAddressService {

	ResponseEntity<AddressDto> saveAddress(AddressCreationDto addressCreationDto);

	ResponseEntity<Void> deleteAddress(Long id);

	ResponseEntity<AddressDto> updateAddress(Long id, AddressCreationDto addressCreationDto);

	ResponseEntity<List<AddressDto>> getAddressesByUserId(String userId);
}
