package com.ecommerce.product.query.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Model;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.query.dto.ProductFilterDto;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class ProductSpecification {
	
	private ProductSpecification() {}
	
	private static final String PRODUCT = "product";
	private static final String PRICE = "price";
	private static final String TITLE = "title";

	public static Specification<Product> getSpecification(ProductFilterDto productFilterDto) {
	    return (root, query, criteriaBuilder) -> {
	        List<Predicate> predicates = new ArrayList<>();
	
	        // Ensure the query returns distinct results
	        query.distinct(true);
	        
	        // Join with categories and filter by multiple categories
	        if (productFilterDto.categories() != null && !productFilterDto.categories().isEmpty()) {
	            Join<Product, Category> categoryJoin = root.join("categories", JoinType.LEFT);
	            predicates.add(categoryJoin.get(TITLE).in(productFilterDto.categories()));
	        }

			// Filter by title
			if (productFilterDto.title() != null && !productFilterDto.title().isEmpty()) {
				predicates.add(criteriaBuilder.like(
						criteriaBuilder.lower(root.get(TITLE)),
						"%" + productFilterDto.title().toLowerCase() + "%"
				));
			}
	
	        // Calculate average rating and filter by minRating
	        if (productFilterDto.minRating() != null) {
	            // Subquery to calculate the average rating for each product
	            Subquery<Double> avgRatingSubquery = query.subquery(Double.class);
	            Root<Model> modelRoot = avgRatingSubquery.from(Model.class);
	            avgRatingSubquery.select(criteriaBuilder.avg(modelRoot.join("ratings").get("value")))
	                             .where(criteriaBuilder.equal(modelRoot.get(PRODUCT), root));
	
	            // Add having clause to filter by the given average rating
	            predicates.add(criteriaBuilder.greaterThanOrEqualTo(avgRatingSubquery, (double) productFilterDto.minRating()));
	        }
	        
	        // Filter by price range
	        if (productFilterDto.minPrice() != null && productFilterDto.maxPrice() != null) {
	            Subquery<Integer> modelSubquery = query.subquery(Integer.class);
	            Root<Model> modelRoot = modelSubquery.from(Model.class);
	            modelSubquery.select(modelRoot.get(PRODUCT).get("id"))
	                    .where(
	                        criteriaBuilder.and(
	                            criteriaBuilder.equal(modelRoot.get(PRODUCT), root),
	                            criteriaBuilder.between(modelRoot.get(PRICE), 
	                                productFilterDto.minPrice(), 
	                                productFilterDto.maxPrice())
	                        )
	                    );
	            predicates.add(criteriaBuilder.exists(modelSubquery));
	        }
			
	        // Filter by minimum price 
			if (productFilterDto.minPrice() != null && productFilterDto.maxPrice() == null) {
				Subquery<Double> minPriceSubquery = query.subquery(Double.class);
				Root<Model> modelRoot = minPriceSubquery.from(Model.class);
				minPriceSubquery.select(criteriaBuilder.min(modelRoot.get(PRICE)))
						.where(criteriaBuilder.equal(modelRoot.get(PRODUCT), root));
				predicates.add(criteriaBuilder.ge(minPriceSubquery, (double) productFilterDto.minPrice()));
			}
			
	        // Filter by maximum price 
			if (productFilterDto.minPrice() == null && productFilterDto.maxPrice() != null) {
				Subquery<Double> maxPriceSubquery = query.subquery(Double.class);
				Root<Model> modelRoot = maxPriceSubquery.from(Model.class);
				maxPriceSubquery.select(criteriaBuilder.max(modelRoot.get(PRICE)))
						.where(criteriaBuilder.equal(modelRoot.get(PRODUCT), root));
				predicates.add(criteriaBuilder.le(maxPriceSubquery, (double) productFilterDto.maxPrice()));
			}
			
			// Filter by model state
			if (productFilterDto.modelState() != null) {
			    Join<Product, Model> modelJoin = root.join("models", JoinType.LEFT);
			    predicates.add(criteriaBuilder.equal(modelJoin.get("state"), productFilterDto.modelState()));
			}
	
	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	    };
	}
}
