package com.ecommerce.product.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "userId", "model_id" }) })
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String userId;
	
	@ManyToOne
	@JoinColumn(name = "model_id", nullable=false)
	private Model model;
	
	@Positive
	private Integer value;
	
	@OneToOne(optional = true, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "commentary_id")
	private Commentary commentary;
}
