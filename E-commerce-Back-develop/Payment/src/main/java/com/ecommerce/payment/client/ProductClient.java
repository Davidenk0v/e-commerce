package com.ecommerce.payment.client;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import com.ecommerce.payment.dto.response.ModelDto;
import com.ecommerce.payment.dto.response.ProductDto;


public interface ProductClient {

    @GetExchange("/api/v1/product/model/{id}")
    public ModelDto getModel(@PathVariable Long id);
    
    @GetExchange("/api/v1/product/product/{id}")
    public ProductDto getProduct(@PathVariable Long id);
}
