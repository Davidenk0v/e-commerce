package com.ecommerce.product.dto.response;

import lombok.Builder;

@Builder
public record ModelStateSummaryDto(
	Long totalModels,
	Long totalModelsApproved,
	Long totalModelsRejected,
	Long totalModelsPendingApproval,
	Long totalModelsOnSale
) {}
