package com.ecommerce.messaging.services;

import com.ecommerce.messaging.dtos.MessageDTO;
import com.ecommerce.messaging.dtos.MessageEvent;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IMessageService {


    ResponseEntity<List<MessageDTO>> getMessagesByUserId(String userId);

    ResponseEntity<MessageDTO> changeMessageState(Long messageId);

    ResponseEntity<?> deleteMessage(Long messageId);

    void sendNotification(MessageEvent event);

    // Método para verificar si un usuario está conectado
    boolean isUserConnected(String userId);
}
