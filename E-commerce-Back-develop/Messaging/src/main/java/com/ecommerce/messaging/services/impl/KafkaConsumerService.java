package com.ecommerce.messaging.services.impl;

import com.ecommerce.messaging.dtos.MessageEvent;
import java.util.function.Consumer;

import com.ecommerce.messaging.dtos.enums.MessageState;
import com.ecommerce.messaging.entities.MessageEntity;
import com.ecommerce.messaging.repositories.MessageRepository;
import com.ecommerce.messaging.services.IKafkaConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService implements IKafkaConsumerService {

    private final MessageService notificationService;

    private final MessageRepository messageRepository;

    @Bean
    public Consumer<MessageEvent> receiveProductMessage() {
        return event -> processMessage("product", event);
    }

    @Bean
    public Consumer<MessageEvent> receiveCartMessage() {
        return event -> processMessage("cart", event);
    }

    @Bean
    public Consumer<MessageEvent> receiveInventoryMessage() {
        return event -> processMessage("inventory", event);
    }

    @Override
    public void processMessage(String topic, MessageEvent event) {
        try {
            System.out.println("🔔 Notificación de " + topic + " para " + event.userId() + ": " + event.message());
            switch (topic) {
                case "product":
                    // Lógica específica para mensajes de productos
                    System.out.println("Procesando mensaje de producto");
                    System.out.println(event.toString());

                    MessageEntity messageEntity = MessageEntity.builder()
                            .userId(event.userId())
                            .message(event.message())
                            .type(event.type())
                            .state(MessageState.PENDING)
                            .ownerId(event.ownerId())
                            .timestamp(event.timestamp())
                            .build();

                    try {
                        messageRepository.save(messageEntity);
                    }catch (Exception e){
                        System.err.println("❌ Error al guardar la notificación: " + e.getMessage());
                    }
                    notificationService.sendNotification(event);
                    break;
                case "cart":
                    // Lógica específica para mensajes de órdenes
                    System.out.println("Procesando mensaje de carro");
                    break;
                case "inventory":
                    // Lógica específica para mensajes de usuarios
                    System.out.println("Procesando mensaje de inventario");
                    break;
                default:
                    System.out.println("Topic no reconocido: " + topic);
            }
        } catch (Exception e) {
            System.err.println("❌ Error al procesar el mensaje de " + topic + ": " + e.getMessage());
        }
    }
}
