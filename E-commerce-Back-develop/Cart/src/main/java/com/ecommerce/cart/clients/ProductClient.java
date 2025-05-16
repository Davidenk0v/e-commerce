package com.ecommerce.cart.clients;

import com.ecommerce.cart.dto.request.ModelDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;


public interface ProductClient {

    @GetExchange("/api/v1/product/model/{id}")
    public ModelDto getModel(@PathVariable Long id);
}
