package com.ecommerce.product.repository;

import com.ecommerce.product.entity.SellerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRequestRepository extends JpaRepository<SellerRequest, Long> {

    List<SellerRequest> findAllByUserId(String userId);
}
