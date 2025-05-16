package com.ecommerce.product.config;

import com.ecommerce.product.clients.UserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${gateway.url}")
    private String userServiceUrl;

    @Bean
    public UserClient productClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(userServiceUrl)
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxy = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxy.createClient(UserClient.class);

    }
}
