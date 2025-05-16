package com.ecommerce.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
					.title("Product Service API")
					.version("1.0.0")
					.description("API to manage products and all the information related to them such as user commentaries, ratings, categories and specifications"))
				.externalDocs(new ExternalDocumentation()
                    .description("You can refer to the project Wiki for more information")
                    .url("https://github.gsissc.myatos.net/ES-TEF-CEDEI-FORMACIONES/E-commerce-Back/wiki"));
	}
}
