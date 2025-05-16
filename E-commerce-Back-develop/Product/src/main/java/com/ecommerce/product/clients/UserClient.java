package com.ecommerce.product.clients;

import com.ecommerce.product.dto.request.UserDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface UserClient {

    @GetExchange("/api/v1/keycloak/user/find-by-id/{id}")
    public UserDto getUserById(@PathVariable String id);
}
