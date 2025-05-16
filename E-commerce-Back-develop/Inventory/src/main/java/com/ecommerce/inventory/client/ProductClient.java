package com.ecommerce.inventory.client;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import com.ecommerce.inventory.dto.response.ModelDto;

public interface ProductClient {

    @GetExchange("/api/v1/product/model/{id}")
    public ModelDto getModel(@PathVariable Long id);
}
