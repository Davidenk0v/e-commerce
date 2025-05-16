package com.ecommerce.payment.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.payment.dto.request.AddressCreationDto;
import com.ecommerce.payment.dto.response.AddressDto;
import com.ecommerce.payment.entity.Address;
import com.ecommerce.payment.mapper.IDtoMapper;
import com.ecommerce.payment.repository.AddressRepository;
import com.ecommerce.payment.service.IAddressService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {
	
	private final AddressRepository addressRepository;
	private final IDtoMapper<AddressCreationDto, Address, AddressDto> addressMapper;	
	
	@Override	
	public ResponseEntity<AddressDto> saveAddress(AddressCreationDto addressCreationDto) {
		// Check if address with the same alias already exists for the user
		if (addressRepository.existsByUserIdAndAddressAlias(addressCreationDto.userId(), addressCreationDto.addressAlias())){
			throw new IllegalArgumentException(
				String.format(
					"Address with the alias '%s' already exists for the user with id '%s",
					addressCreationDto.addressAlias(), 
					addressCreationDto.userId()));
		}
	
		Address address = addressMapper.toEntity(addressCreationDto);
		address = addressRepository.save(address);
		AddressDto addressDto = addressMapper.toDto(address);
		
		return ResponseEntity.status(201).body(addressDto);
	}

	@Override
	public ResponseEntity<Void> deleteAddress(Long id) {
		if (!addressRepository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		addressRepository.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Override
	public ResponseEntity<AddressDto> updateAddress(Long id, AddressCreationDto addressCreationDto) {		
		// Find the address by its id and update its fields, if not found return NOT_FOUND
		Optional<Address> addressOpt = addressRepository.findById(id);
		if (addressOpt.isPresent()) {
			Address address = addressOpt.get();
			// Check if a different address with the same alias already exists for the user
			if (addressRepository.existsByUserIdAndAddressAliasAndIdNot(address.getUserId(), addressCreationDto.addressAlias(),	id)) {
				throw new IllegalArgumentException(
						String.format(
								"Address with the alias '%s' already exists for the user with id '%s'",
								addressCreationDto.addressAlias(), 
								address.getUserId()));
			}
			
			address.setLocality(addressCreationDto.locality());
			address.setProvince(addressCreationDto.province());
			address.setStreet(addressCreationDto.street());
			address.setAddressAlias(addressCreationDto.addressAlias());
			address.setCp(addressCreationDto.cp());
			address = addressRepository.save(address);
			AddressDto addressDto = addressMapper.toDto(address);
			return ResponseEntity.status(HttpStatus.OK).body(addressDto);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@Override
	public ResponseEntity<List<AddressDto>> getAddressesByUserId(String userId) {
		List<Address> adresses = addressRepository.findByUserId(userId);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(adresses.stream().map(addressMapper::toDto).toList());
	}
	
	
}
