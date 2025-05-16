package com.ecommerce.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${keycloakserver.url}")
    private String serverUrl;

    private static final String REALM_MASTER = "master";
    private static final String ADMIN_CLI = "admin-cli";
    private static final String USER_CONSOLE = "admin";
    private static final String PASSWORD_CONSOLE = "admin";
    private static final String CLIENT_SECRET = "Gi43YTdIowhrAlGn0DMzAfguW0wM5YW1";
    
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(REALM_MASTER)
                .clientId(ADMIN_CLI)
                .username(USER_CONSOLE)
                .password(PASSWORD_CONSOLE)
                .clientSecret(CLIENT_SECRET)
                .build();
    }
}
