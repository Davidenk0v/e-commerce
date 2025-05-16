package com.ecommerce.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.payment.entity.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

	Optional<Bill> findByOrderId(Long orderId);

}
