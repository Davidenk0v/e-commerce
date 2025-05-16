package com.ecommerce.product.service.impl;

import com.ecommerce.product.dto.response.MessageEvent;
import com.ecommerce.product.entity.MessageType;
import com.ecommerce.product.service.IKafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService implements IKafkaService {

    private final StreamBridge streamBridge;


    @Override
    public void sendProductMessage(MessageEvent messageEvent) {
        streamBridge.send("sendProductMessage-out-0", messageEvent);
    }

//    public void sendCartMessage(String userId, String message) {
//        MessageEvent messageEvent = new MessageEvent("cart", message, String.valueOf(System.currentTimeMillis()));
//        System.out.println("🔔 Enviando notificación desde cart");
//        streamBridge.send("cart-topic", messageEvent);
//    }
//
//    public void sendInventoryMessage(String userId, String message) {
//        MessageEvent messageEvent = new MessageEvent(userId, message, String.valueOf(System.currentTimeMillis()));
//        System.out.println("🔔 Enviando notificación desde Inventory");
//        streamBridge.send("inventory-topic", messageEvent);
//    }

}
