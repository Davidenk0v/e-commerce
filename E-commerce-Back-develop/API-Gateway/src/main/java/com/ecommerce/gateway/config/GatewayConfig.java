package com.ecommerce.gateway.config;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("CART-SERVICE", r -> r
                        .path("/api/v1/cart/**")
                        .uri("lb://CART-SERVICE"))
                .route("PRODUCT-SERVICE", r -> r
                        .path("/api/v1/product/**")
                        .uri("lb://PRODUCT-SERVICE"))
                .route("PAYMENT-SERVICE", r -> r
                        .path("/api/v1/payment/**")
                        .uri("lb://PAYMENT-SERVICE"))
                .route("INVENTORY-SERVICE", r -> r
                        .path("/api/v1/inventory/**")
                        .uri("lb://INVENTORY-SERVICE"))
                .route("MESSAGING-SERVICE", r -> r
                        .path("/api/v1/messaging/**")
                        .uri("lb://MESSAGING-SERVICE"))
                .build();
    }
}

