package com.ecommerce.product.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
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
public class Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id", nullable=false)
	private Product product;
	
	@Positive
	private BigDecimal price;
	
	@NotBlank
	@Column(columnDefinition = "TEXT") // Specify TEXT type in PostgreSQL
	private String description;
	
	@OneToMany
	@JoinTable(
        name = "model_image",
        joinColumns = @JoinColumn(name = "model_id"), 
        inverseJoinColumns = @JoinColumn(name = "image_id"))
	private List<Image> images;
	
	@ManyToMany
	@JoinTable(
        name = "model_spec",
        joinColumns = @JoinColumn(name = "model_id"), 
        inverseJoinColumns = @JoinColumn(name = "spec_id"))
	private List<Spec> specs;
	
	@NotNull
	private ModelState state;
	
	@OneToMany(mappedBy = "model", cascade = CascadeType.REMOVE)
	private List<Rating> ratings;
	
	@OneToMany(mappedBy = "model", cascade = CascadeType.REMOVE)
	private List<Commentary> commentaries;
}
