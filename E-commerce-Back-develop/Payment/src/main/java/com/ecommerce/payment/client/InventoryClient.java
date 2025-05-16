package com.ecommerce.payment.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.ecommerce.payment.dto.request.StockCreationDto;
import com.ecommerce.payment.dto.response.StockDto;

public interface InventoryClient {

    @GetExchange("/api/v1/inventory/stock/total/{modelId}")
    public Long getTotalStock(@PathVariable Long modelId);
    
    @GetExchange("/api/v1/inventory/stock/model/{modelId}/warehouse/{warehouseId}")
    public StockDto getStock(@PathVariable Long modelId, @PathVariable Long warehouseId);
    
    @PutExchange("/api/v1/inventory/stock/{id}")
    public StockDto updateStock(@PathVariable Long id, @RequestBody StockCreationDto stockCreationDto);
}
