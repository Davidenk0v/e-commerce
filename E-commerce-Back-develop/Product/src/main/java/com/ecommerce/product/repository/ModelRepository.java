package com.ecommerce.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.product.entity.Model;
import com.ecommerce.product.entity.ModelState;
import com.ecommerce.product.entity.Spec;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

	List<Model> findByProductId(Long id);
	
	List<Model> findByProductTitle(String title);

	List<Model> findByProductIdAndPriceBetween(Long id, Integer minPrice, Integer maxPrice);

	List<Model> findByProductIdAndPriceGreaterThanEqual(Long id, Integer minPrice);

	List<Model> findByProductIdAndPriceLessThanEqual(Long id, Integer maxPrice);
	
    @Query("SELECT m FROM Model m JOIN m.product p JOIN m.specs s WHERE p.id = :productId AND s IN :specs GROUP BY m HAVING COUNT(DISTINCT s) = :specCount")
    List<Model> findByProductIdAndSpecs(@Param("productId") Long productId, @Param("specs") List<Spec> specs, @Param("specCount") long specCount);

	Long countByProductSellerIdAndState(String sellerId, ModelState approved);

	Long countByProductSellerId(String sellerId);
}
