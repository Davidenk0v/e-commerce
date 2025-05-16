package com.ecommerce.product.service;

import com.ecommerce.product.dto.response.MessageEvent;

public interface IKafkaService {
    void sendProductMessage(MessageEvent messageEvent);
}
