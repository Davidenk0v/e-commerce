package com.ecommerce.cart.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
					.title("Cart Service API")
					.version("1.0.0")
					.description("API to manage cart and wanted products"))
				.externalDocs(new ExternalDocumentation()
                    .description("You can refer to the project Wiki for more information")
                    .url("https://github.gsissc.myatos.net/ES-TEF-CEDEI-FORMACIONES/E-commerce-Back/wiki"));
	}
}
