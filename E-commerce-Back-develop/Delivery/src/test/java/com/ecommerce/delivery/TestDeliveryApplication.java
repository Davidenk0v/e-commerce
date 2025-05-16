package com.ecommerce.delivery;

import org.springframework.boot.SpringApplication;

public class TestDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.from(DeliveryApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
