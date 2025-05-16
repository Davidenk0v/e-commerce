package com.ecommerce.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.inventory.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

	Optional<Stock> findByWarehouseIdAndModelId(Long warehouseId, Long modelId);

	List<Stock> findByModelId(Long modelId);
}
