package com.ecommerce.messaging.controller;

import com.ecommerce.messaging.dtos.MessageDTO;
import com.ecommerce.messaging.dtos.UserConnectionMessage;
import com.ecommerce.messaging.services.IMessageService;
import com.ecommerce.messaging.services.impl.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/messaging/notification")
public class NotificationController {

    private final IMessageService notificationService;

//    @GetMapping("/test-ws/{userId}")
//    public ResponseEntity<String> testWebSocket(@PathVariable String userId) {
//        // Crear un mensaje de prueba con el formato exacto esperado por el frontend
//        String message = "{\"content\":\"Mensaje de prueba " + System.currentTimeMillis() + "\"}";
//
//        // Log antes de enviar
//        log.debug("Enviando mensaje a usuario: {}", userId);
//        log.debug("Destino: /user/{}/queue/notifications", userId);
//        log.debug("Mensaje: {}", message);
//
//        // Enviar mensaje
//        notificationService.sendNotification(userId, message);
//
//        return ResponseEntity.ok("Mensaje enviado a: " + userId);
//    }

    @MessageMapping("/connect")
    public void handleUserConnect(@Payload UserConnectionMessage message) {
        log.info("Usuario conectado: {}", message.userId());
        // Registrar la conexión del usuario si estás usando el método mejorado
        if (notificationService instanceof MessageService) {
            ((MessageService) notificationService).registerUserConnection(message.userId(), true);
        }
    }

    @MessageMapping("/disconnect")
    public void handleUserDisconnect(@Payload UserConnectionMessage message) {
        log.info("Usuario desconectado: {}", message.userId());
        // Registrar la desconexión del usuario si estás usando el método mejorado
        if (notificationService instanceof MessageService) {
            ((MessageService) notificationService).registerUserConnection(message.userId(), false);
        }
    }

    @GetMapping("/messages/{userId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByUserId(@PathVariable String userId) {
        return notificationService.getMessagesByUserId(userId);
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<MessageDTO> getMessagesByUserId(@PathVariable Long messageId) {
        return notificationService.changeMessageState(messageId);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessageById(@PathVariable Long messageId) {
        return notificationService.deleteMessage(messageId);
    }
}
