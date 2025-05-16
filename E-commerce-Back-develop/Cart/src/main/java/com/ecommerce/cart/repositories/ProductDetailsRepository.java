package com.ecommerce.cart.repositories;

import com.ecommerce.cart.entities.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long> {
    List<ProductDetails> findByCartId(Long cartId);
}
