package com.ecommerce.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.ecommerce.product.dto.request.SpecCreationDto;
import com.ecommerce.product.dto.response.SpecDto;

public interface ISpecService {

	ResponseEntity<SpecDto> saveSpec(SpecCreationDto specCreationDto);
	
	ResponseEntity<SpecDto> getSpec(Long id);
	
	ResponseEntity<List<SpecDto>> getSpecs();
	
	ResponseEntity<List<SpecDto>> getSpecsByName(Optional<String> name);
	
	ResponseEntity<Void> deleteSpec(Long id);
	
	ResponseEntity<SpecDto> updateSpec(Long id, SpecCreationDto specCreationDto);
}
