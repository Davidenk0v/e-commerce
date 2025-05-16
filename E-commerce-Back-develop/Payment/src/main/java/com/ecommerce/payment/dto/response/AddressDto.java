package com.ecommerce.payment.dto.response;

import lombok.Builder;

@Builder
public record AddressDto(
	Long id,
	String userId,
	String locality,
	String province,
	String street,
	String addressAlias,
	String cp
) {}
