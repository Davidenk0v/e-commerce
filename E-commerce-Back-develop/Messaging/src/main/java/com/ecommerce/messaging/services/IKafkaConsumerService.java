package com.ecommerce.messaging.services;

import com.ecommerce.messaging.dtos.MessageEvent;

public interface IKafkaConsumerService {
    void processMessage(String topic, MessageEvent event);
}
