package com.ecommerce.inventory.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Warehouse {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String localization;
	
	@NotBlank
	private String description;
	
	@Email
	private String email;
	
	@Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
	private String phoneNumber;
	
	@OneToMany(mappedBy = "warehouse")
	private List<Stock> stocks;
}
