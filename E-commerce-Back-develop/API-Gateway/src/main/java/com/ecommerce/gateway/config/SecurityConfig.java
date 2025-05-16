package com.ecommerce.gateway.config;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${keycloakserver.url}")
    private String serverUrl;
	
    private final ReactiveJwtAuthenticationConverter jwtAuthenticationConverter;

    private static final String USER_ROLE = "ROLE_USER_E-COMMERCE";
    private static final String ADMIN_ROLE = "ROLE_ADMIN_E-COMMERCE";
    private static final String VENDOR_ROLE = "ROLE_VENDOR_E-COMMERCE";
    private static final String PRODUCT_SERVICE_URL = "/api/v1/product";

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/keycloak/user/**").permitAll()
                        .pathMatchers("/api/v1/*").permitAll()
                        .pathMatchers(PRODUCT_SERVICE_URL + "/seller-request/**").hasAnyAuthority(VENDOR_ROLE, ADMIN_ROLE)
                        .pathMatchers(PRODUCT_SERVICE_URL + "/product/of-seller/**").hasAnyAuthority(VENDOR_ROLE, ADMIN_ROLE)
                        .pathMatchers("/v3/api-docs").permitAll()
                        .pathMatchers("/swagger-resources").permitAll()
                        .pathMatchers("/swagger-resources/**").permitAll()
                        .pathMatchers("/configuration/ui").permitAll()
                        .pathMatchers("/configuration/security").permitAll()
                        .pathMatchers("/swagger-ui/**").permitAll()
                        .pathMatchers("/webjars/**").permitAll()
                        .pathMatchers("/swagger-ui.html").permitAll()
                        .anyExchange().permitAll())
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        String issuerUri = serverUrl + "/realms/e-commerce_realm/protocol/openid-connect/certs";
        return NimbusReactiveJwtDecoder.withJwkSetUri(issuerUri).build();
    }

}