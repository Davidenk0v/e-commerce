package com.ecommerce.inventory.seeder;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ecommerce.inventory.entity.Stock;
import com.ecommerce.inventory.entity.Warehouse;
import com.ecommerce.inventory.repository.StockRepository;
import com.ecommerce.inventory.repository.WarehouseRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {

	private final WarehouseRepository warehouseRepository;
	private final StockRepository stockRepository;
	
	@Override
	@Transactional	
	public void run(String... args) throws Exception {
		// If no warehouses exist assume that the database is empty and seed it 
		if (warehouseRepository.count() == 0) {
			// Seed warehouses
			warehouseRepository.saveAll(List.of(
					Warehouse.builder()
						.localization("Madrid")
						.description("Almacen que suministra a la zona centro de la península")
						.email("whmadrid@gmail.com")
						.phoneNumber("+34912345678")
						.build(),
					Warehouse.builder()
						.localization("Zaragoza")
						.description("Almacen que suministra a la zona norte de la península")
						.email("whzaragoza@gmail.com")
						.phoneNumber("+34917365679")
						.build()));
			
			// Seed stocks
			Optional<Warehouse> optWarehouse1 = warehouseRepository.findById(1L);
			Optional<Warehouse> optWarehouse2 = warehouseRepository.findById(2L);
			Warehouse warehouse1 = null;
			Warehouse warehouse2 = null;
			if (optWarehouse1.isPresent() && optWarehouse2.isPresent()) {
				warehouse1 = optWarehouse1.get();
				warehouse2 = optWarehouse2.get();
			}
			stockRepository.saveAll(List.of(
						Stock.builder()
							.quantity(100L)
							.warehouse(warehouse1)
							.modelId(1L)
							.build(),
						Stock.builder()
							.quantity(50L)
							.warehouse(warehouse2)
							.modelId(1L)
							.build(),
						Stock.builder()
							.quantity(100L)
							.warehouse(warehouse1)
							.modelId(2L)
							.build(),
						Stock.builder()
							.quantity(100L)
							.warehouse(warehouse1)
							.modelId(3L)
							.build(),
						Stock.builder()
							.quantity(100L)
							.warehouse(warehouse1)
							.modelId(4L)
							.build(),
						Stock.builder()
							.quantity(100L)
							.warehouse(warehouse1)
							.modelId(5L)
							.build(),
						Stock.builder()
							.quantity(100L)
							.warehouse(warehouse1)
							.modelId(6L)
							.build(),
						Stock.builder()
							.quantity(100L)
							.warehouse(warehouse1)
							.modelId(7L)
							.build(),
						Stock.builder()
							.quantity(100L)
							.warehouse(warehouse1)
							.modelId(8L)
							.build(),
						Stock.builder()
							.quantity(50L)
							.warehouse(warehouse1)
							.modelId(9L)
							.build()));
		}
	}
}
