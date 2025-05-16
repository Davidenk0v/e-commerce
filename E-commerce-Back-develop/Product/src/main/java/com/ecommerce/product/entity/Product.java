package com.ecommerce.product.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "title", "sellerId" }) })
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String title;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "category_product",
		joinColumns = @JoinColumn(name = "product_id"), 
		inverseJoinColumns = @JoinColumn(name = "category_id"))
	private List<Category> categories;
	
	@NotBlank
	private String sellerId;
	
	@NotBlank
	private String manufacturer;
	
	@OneToMany(mappedBy = "product")
	private List<Model> models;
}
