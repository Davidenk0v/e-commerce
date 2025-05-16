package com.ecommerce.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.ecommerce.payment.client.InventoryClient;
import com.ecommerce.payment.client.ProductClient;

@Configuration
public class RestClientConfig {

    @Value("${product.url}")
    private String productServiceUrl;
    
    @Value("${inventory.url}")
    private String inventoryServiceUrl;

    @Bean
    public ProductClient productClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(productServiceUrl)
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxy = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxy.createClient(ProductClient.class);

    }
    
    @Bean
    public InventoryClient inventoryClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(inventoryServiceUrl)
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxy = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxy.createClient(InventoryClient.class);

    }
}
