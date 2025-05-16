package com.ecommerce.payment.query.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ecommerce.payment.entity.Order;
import com.ecommerce.payment.query.dto.OrderFilterDto;

import jakarta.persistence.criteria.Predicate;

public class OrderSpecification {

	private OrderSpecification() {}
	
	public static Specification<Order> getSpecification(OrderFilterDto orderFilterDto) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			
			// Filter by order state
			if (orderFilterDto.state() != null) {
                predicates.add(criteriaBuilder.equal(root.get("state"), orderFilterDto.state()));
            }
			
			// Filter by userId
			if (orderFilterDto.userId() != null) {
				predicates.add(criteriaBuilder.equal(root.get("userId"), orderFilterDto.userId()));
			}
			
			// Filter by month and year
			if (orderFilterDto.year() != null) {
			    // Start with the year predicate
			    Predicate yearPredicate = criteriaBuilder.equal(
			    	criteriaBuilder.function(
			    		"date_part", 
			    		Integer.class, 
			    		criteriaBuilder.literal("YEAR"),
			    		root.get("creationDate")), 
			    		orderFilterDto.year());
			    // If month is also provided, add the month predicate
			    if (orderFilterDto.month() != null) {
			        Predicate monthPredicate = criteriaBuilder.equal(
			        	criteriaBuilder.function(
			        		"date_part", 
			        		Integer.class,
			        		criteriaBuilder.literal("MONTH"),
			        		root.get("creationDate")), 
			        		orderFilterDto.month());
			        predicates.add(criteriaBuilder.and(yearPredicate, monthPredicate));
			    } else {
			        // Only year is provided, so add year predicate to the list
			        predicates.add(yearPredicate);
			    }
			}
			
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
