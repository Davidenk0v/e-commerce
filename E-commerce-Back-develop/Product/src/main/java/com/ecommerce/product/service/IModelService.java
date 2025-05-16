package com.ecommerce.product.service;

import java.util.List;

import com.ecommerce.product.entity.ModelState;
import org.springframework.http.ResponseEntity;

import com.ecommerce.product.dto.request.ModelCreationDto;
import com.ecommerce.product.dto.response.ModelDto;
import com.ecommerce.product.dto.response.ModelStateSummaryDto;

public interface IModelService {

	ResponseEntity<ModelDto> saveModel(ModelCreationDto modelCreationDto);

	ResponseEntity<ModelDto> getModel(Long id);

	ResponseEntity<List<ModelDto>> getModels();

	ResponseEntity<Void> deleteModel(Long id);

	ResponseEntity<ModelDto> updateModel(Long id, ModelCreationDto modelCreationDto);


	ResponseEntity<ModelDto> changesStateModel(Long id, ModelState state);

	ResponseEntity<List<ModelDto>> approveModels(List<Long> ids);

	ResponseEntity<List<ModelDto>> rejectModels(List<Long> ids);
	ResponseEntity<ModelStateSummaryDto> getModelStateSummary(String sellerId);
	
	
}
