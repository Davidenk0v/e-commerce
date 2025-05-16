package com.ecommerce.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.inventory.entity.Warehouse;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

	Optional<Warehouse> findByPhoneNumber(String phoneNumber);
	
	Optional<Warehouse> findByEmail(String email);

}
