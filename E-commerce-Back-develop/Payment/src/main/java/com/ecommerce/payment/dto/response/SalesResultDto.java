package com.ecommerce.payment.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;

@Builder
public record SalesResultDto(
	BigDecimal totalSales,
	List<Long> salesByMonth
) {}
