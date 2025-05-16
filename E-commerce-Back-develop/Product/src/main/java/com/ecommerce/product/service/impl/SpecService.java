package com.ecommerce.product.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.product.dto.request.SpecCreationDto;
import com.ecommerce.product.dto.response.SpecDto;
import com.ecommerce.product.entity.Spec;
import com.ecommerce.product.mapper.IDtoMapper;
import com.ecommerce.product.repository.SpecRepository;
import com.ecommerce.product.service.ISpecService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpecService implements ISpecService {

	private final SpecRepository specRepository;
	private final IDtoMapper<SpecCreationDto, Spec, SpecDto> specMapper;

	@Override
	public ResponseEntity<SpecDto> saveSpec(SpecCreationDto specCreationDto) {
		Optional<Spec> specOptional = specRepository.findByNameAndValue(specCreationDto.name(), specCreationDto.value());
		if (specOptional.isPresent()) {
			throw new IllegalArgumentException(
					String.format("Spec with name '%s' and value '%s' already exists", specCreationDto.name(), specCreationDto.value()));
		}
		Spec spec = specMapper.toEntity(specCreationDto);
		spec = specRepository.save(spec);
		SpecDto specDto = specMapper.toDto(spec);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(specDto);
	}

	@Override
	public ResponseEntity<SpecDto> getSpec(Long id) {
		Optional<Spec> specOptional = specRepository.findById(id);
		if (specOptional.isPresent()) {
			SpecDto specDto = specMapper.toDto(specOptional.get());
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(specDto);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@Override
	public ResponseEntity<List<SpecDto>> getSpecs() {
		List<Spec> specs = specRepository.findAll();
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(specs.stream().map(specMapper::toDto).toList());
	}

	@Override
	public ResponseEntity<SpecDto> updateSpec(Long id, SpecCreationDto specCreationDto) {
		Optional<Spec> specOptional = specRepository.findByNameAndValue(specCreationDto.name(), specCreationDto.value());
		if (specOptional.isPresent()) {
            throw new IllegalArgumentException(
                String.format("Spec with name '%s' and value '%s' already exists", specCreationDto.name(), specCreationDto.value()));
        }
		specOptional = specRepository.findById(id);
		if (specOptional.isPresent()) {
			Spec spec = specOptional.get();
			spec.setName(specCreationDto.name());
			spec.setValue(specCreationDto.value());
			spec = specRepository.save(spec);
			SpecDto specDto = specMapper.toDto(spec);
			return ResponseEntity
				.status(HttpStatus.OK)
				.body(specDto);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@Override
	public ResponseEntity<Void> deleteSpec(Long id) {
		Optional<Spec> specOptional = specRepository.findById(id);
		if (specOptional.isPresent()) {
			specRepository.delete(specOptional.get());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@Override
	public ResponseEntity<List<SpecDto>> getSpecsByName(Optional<String> optionalName) {
		if (optionalName.isEmpty()) {
			return getSpecs();
		}
		List<Spec> specs = specRepository.findAllByName(optionalName.get());
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(specs.stream().map(specMapper::toDto).toList());
	}

}
