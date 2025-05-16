package com.ecommerce.product.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.product.dto.request.ModelCreationDto;
import com.ecommerce.product.dto.response.ModelDto;
import com.ecommerce.product.dto.response.ModelStateSummaryDto;
import com.ecommerce.product.entity.Model;
import com.ecommerce.product.entity.ModelState;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.entity.Spec;
import com.ecommerce.product.mapper.IDtoMapper;
import com.ecommerce.product.repository.ModelRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.repository.SpecRepository;
import com.ecommerce.product.service.IModelService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModelService implements IModelService {

	private final ProductRepository productRepository;
	private final ModelRepository modelRepository;
	private final SpecRepository specRepository;
	private final IDtoMapper<ModelCreationDto, Model, ModelDto> modelMapper;
	        
	// Create a Safelist that allows basic tags plus <h2>
	private final static Safelist safelist = Safelist.basic().addTags("h2");
	
	@Override
	public ResponseEntity<ModelDto> saveModel(ModelCreationDto modelCreationDto) {
		// Check if there is a product id and if the product exists
		Optional<Product> productOptional = productRepository.findById(modelCreationDto.productId());
		if (!productOptional.isPresent()) {
			throw new IllegalArgumentException(
				String.format("El producto con id '%s' no existe", modelCreationDto.productId()));
		}
		
		//Sanitize the product description
		String sanitizedDescription = Jsoup.clean(modelCreationDto.description(), safelist);
		ModelCreationDto sanitizedModelCreationDto = ModelCreationDto.builder()
											.description(sanitizedDescription)
											.price(modelCreationDto.price())
											.productId(modelCreationDto.productId())
											.specIds(modelCreationDto.specIds())
											.build();
				
		// Initialize the model
		Model model = modelMapper.toEntity(sanitizedModelCreationDto);
		
		// Get the specs for the model
		List<Spec> specs = new ArrayList<>();
		for (Long specId : modelCreationDto.specIds()) {
			Optional<Spec> optSpec = specRepository.findById(specId);
			if (optSpec.isPresent()) {
                Spec spec = optSpec.get();
                specs.add(spec);
			} else {
				throw new IllegalArgumentException(String.format("No se encuentra la especificación '%s'", specId));
			}	
		}
		
		// Check that there are no model with the same combination of specs
		List<Model> existingModelWithSpecs = modelRepository.findByProductIdAndSpecs(
				modelCreationDto.productId(), specs, specs.size());
		if (!existingModelWithSpecs.isEmpty()) {
			throw new IllegalArgumentException("Ya existe un modelo con las especificaciones dadas");
		}	
		
		// Set the specs for the model
		model.setSpecs(specs);
		model = modelRepository.save(model);
		
		// Build a model DTO and return it in the response
		ModelDto modelDto = modelMapper.toDto(model);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(modelDto);
	}

	@Override
	public ResponseEntity<ModelDto> getModel(Long id) {
		Optional<Model> modelOptional = modelRepository.findById(id);
		if (modelOptional.isPresent()) {
			ModelDto modelDto = modelMapper.toDto(modelOptional.get());
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(modelDto);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@Override
	public ResponseEntity<List<ModelDto>> getModels() {
		List<Model> models = modelRepository.findAll();
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(models.stream().map(modelMapper::toDto).toList());
	}
	
	@Override
	public ResponseEntity<Void> deleteModel(Long id) {
		Optional<Model> optModel = modelRepository.findById(id);
		if (optModel.isPresent()) {
			Model model = optModel.get();
			Optional<Product> optProduct = productRepository.findById(model.getProduct().getId());
			if (optProduct.isPresent()) {
				Product product = optProduct.get();
				product.getModels().remove(model);
				productRepository.save(product);
			} else {
                throw new IllegalArgumentException(
                    String.format(
                    		"Product with id '%s', related to existing Model with id '%s', does not exist", 
                    		model.getProduct().getId(), 
                    		model.getId()));
			}
			modelRepository.delete(model);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@Override
	public ResponseEntity<ModelDto> updateModel(Long id, ModelCreationDto modelCreationDto) {
		// Checks if the model to be updated exists
		Optional<Model> modelOptional = modelRepository.findById(id);
		if (modelOptional.isPresent()) {
			Model model = modelOptional.get();
			
			// Gets the specs for the model
			List<Spec> specs = new ArrayList<>();
			for (Long specId : modelCreationDto.specIds()) {
				Optional<Spec> optSpec = specRepository.findById(specId);
				if (optSpec.isPresent()) {
	                Spec spec = optSpec.get();
	                specs.add(spec);
				} else {
					throw new IllegalArgumentException(String.format("Spec with id '%s' not found", specId));
				}	
			}
			
			//Sanitize the product description
			String sanitizedDescription = Jsoup.clean(modelCreationDto.description(), safelist);
			
			// Set the new values and change state to PENDING_APPROVAL
			model.setDescription(sanitizedDescription);
			model.setSpecs(specs);
			model.setPrice(modelCreationDto.price());
			model.setState(ModelState.PENDING_APPROVAL);
			model = modelRepository.save(model);
			
			// Return the updated model as a DTO
			ModelDto modelDto = modelMapper.toDto(model);
			return ResponseEntity
				.status(HttpStatus.OK)
				.body(modelDto);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@Override
	public ResponseEntity<ModelDto> changesStateModel(Long id, ModelState state) {
		Optional<Model> modelOptional = modelRepository.findById(id);
		if (modelOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
			Model model = modelOptional.get();
			model.setState(state);
			modelRepository.save(model);

		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.toDto(model));
	}

	@Override
	public ResponseEntity<List<ModelDto>> approveModels(List<Long> ids) {
		List<Model> models = modelRepository.findAllById(ids);
		if (models.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		for (Model model : models) {
			model.setState(ModelState.APPROVED);
			modelRepository.save(model);
		}

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * Rejects a list of models by setting their state to REJECTED.
	 *
	 * @param ids the list of model IDs to reject
	 * @return a ResponseEntity indicating the result of the operation
	 */
	@Override
	public ResponseEntity<List<ModelDto>> rejectModels(List<Long> ids) {
		List<Model> models = modelRepository.findAllById(ids);
		if (models.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		for (Model model : models) {
			model.setState(ModelState.REJECTED);
			modelRepository.save(model);
		}

		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	public ResponseEntity<ModelStateSummaryDto> getModelStateSummary(String sellerId) {
		Long totalModels = modelRepository.countByProductSellerId(sellerId);
		Long totalModelsApproved = modelRepository.countByProductSellerIdAndState(sellerId, ModelState.APPROVED);
		Long totalModelsRejected = modelRepository.countByProductSellerIdAndState(sellerId, ModelState.REJECTED);
		Long totalModelsPendingApproval = modelRepository.countByProductSellerIdAndState(sellerId, ModelState.PENDING_APPROVAL);
		Long totalModelsOnSale = modelRepository.countByProductSellerIdAndState(sellerId, ModelState.ON_SALE);

		ModelStateSummaryDto summaryDto = ModelStateSummaryDto.builder()
				.totalModels(totalModels)
				.totalModelsApproved(totalModelsApproved)
				.totalModelsRejected(totalModelsRejected)
				.totalModelsPendingApproval(totalModelsPendingApproval)
				.totalModelsOnSale(totalModelsOnSale)
				.build();

		return ResponseEntity.status(HttpStatus.OK).body(summaryDto);
	}
}
