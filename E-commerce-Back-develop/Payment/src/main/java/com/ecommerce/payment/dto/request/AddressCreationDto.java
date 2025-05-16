package com.ecommerce.payment.dto.request;

import lombok.Builder;

@Builder
public record AddressCreationDto(
	String userId,
	String locality,
	String province,
	String street,
	String addressAlias,
	String cp
) {}
