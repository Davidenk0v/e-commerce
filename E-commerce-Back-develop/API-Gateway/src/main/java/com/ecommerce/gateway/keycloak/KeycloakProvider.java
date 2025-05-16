package com.ecommerce.gateway.keycloak;
import java.io.IOException;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class KeycloakProvider {
	
    @Value("${keycloakserver.url}")
    private String serverUrl;
	
    public static final String REALM_NAME = "e-commerce_realm";
    public static final String CLIENT_ID = "e-commerce_client";
    public static final String CLIENT_SECRET = "Gi43YTdIowhrAlGn0DMzAfguW0wM5YW1";
    
    private final Keycloak keycloak;

    public KeycloakProvider(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public RealmResource getRealmResource() {
        return keycloak.realm(REALM_NAME);
    }

    public UsersResource getUserResource() {
        return getRealmResource().users();
    }
    
    public Keycloak getKeycloak() {
        return this.keycloak;
    }
    
    public AccessTokenResponse getToken(String username, String password) {
        Keycloak keycloakClient = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(REALM_NAME)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .username(username)
                .password(password)
                .build();

        return keycloakClient.tokenManager().getAccessToken();
    }
    
    public AccessTokenResponse refreshToken(String refreshToken) {
        try {
            String url = serverUrl + "/realms/" + REALM_NAME + "/protocol/openid-connect/token";
            
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("grant_type", "refresh_token")
                    .add("client_id", CLIENT_ID)
                    .add("client_secret", CLIENT_SECRET)
                    .add("refresh_token", refreshToken)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            
	        Response response = client.newCall(request).execute();
	        
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            ObjectMapper objectMapper = new ObjectMapper();
            
            return objectMapper.readValue(response.body().string(), AccessTokenResponse.class);

        } catch (Exception e) {
            throw new RuntimeException("Error refreshing token", e);
        }
    }
}
