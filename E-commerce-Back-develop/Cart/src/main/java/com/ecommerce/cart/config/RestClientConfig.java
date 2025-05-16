package com.ecommerce.cart.config;

import com.ecommerce.cart.clients.ProductClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${product.url}")
    private String productServiceUrl;

    @Bean
    public ProductClient productClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(productServiceUrl)
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxy = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxy.createClient(ProductClient.class);

    }
}
