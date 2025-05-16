package com.ecommerce.payment.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Bill {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String cardBrand;
	
	@NotBlank
	private String cardNumber;
	
	@Min(1)
	@Max(12)
	private Long cardExpMonth;
	
	@Min(2020)
	private Long cardExpYear;
	
	@Min(50)
	private Long amountReceived;
	
	@NotBlank
	private String currency;
	
	@OneToOne
	private Order order;
	
	@CreationTimestamp
	private Instant creationDate;
	
	@NotBlank
	private String street;
	
	@NotBlank
	private String locality;
	
	@NotBlank
	private String province;
	
	@NotBlank
	private String cp;
	
	@NotBlank
	private String country;
}
