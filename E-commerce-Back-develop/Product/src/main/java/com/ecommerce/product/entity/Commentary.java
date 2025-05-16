package com.ecommerce.product.entity;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "userId", "parent_commentary_id" }) })
public class Commentary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "model_id", nullable = false)
	private Model model;
	
	@OneToMany
	@JoinTable(
        name = "commentary_image",
        joinColumns = @JoinColumn(name = "commentary_id"), 
        inverseJoinColumns = @JoinColumn(name = "image_id"))
	private List<Image> images;
	
	@CreationTimestamp
	private Instant creationDate;
	
	@UpdateTimestamp
	private Instant updateDate;
	
	@NotBlank
	private String userId;
	
	@NotBlank
	private String text;
	
	@OneToOne(mappedBy = "commentary")
	private Rating rating;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_commentary_id")
	private Commentary parentCommentary;
	
	@OneToMany(mappedBy = "parentCommentary", cascade = CascadeType.REMOVE)
	private List<Commentary> childCommentaries;
	
	@OneToMany(mappedBy = "commentary", cascade = CascadeType.REMOVE)
	private List<Like> likes;
}
