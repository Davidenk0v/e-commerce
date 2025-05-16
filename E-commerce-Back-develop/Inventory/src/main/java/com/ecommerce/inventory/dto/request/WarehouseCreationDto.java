package com.ecommerce.inventory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record WarehouseCreationDto(
	@Schema(example="Toledo")
	String localization,
	@Schema(example="Almacen que suministra la zona centro de España")
	String description,
	@Schema(example="pablocabrera@gmail.com")
	String email,
	@Schema(example="34666777888")
	String phoneNumber
) {}
