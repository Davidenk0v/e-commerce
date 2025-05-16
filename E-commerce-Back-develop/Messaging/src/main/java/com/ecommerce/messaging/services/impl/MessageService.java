package com.ecommerce.messaging.services.impl;

import com.ecommerce.messaging.dtos.MessageDTO;
import com.ecommerce.messaging.dtos.MessageEvent;
import com.ecommerce.messaging.dtos.enums.MessageState;
import com.ecommerce.messaging.entities.MessageEntity;
import com.ecommerce.messaging.mappers.IDtoMapper;
import com.ecommerce.messaging.repositories.MessageRepository;
import com.ecommerce.messaging.services.IMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {

    private final SimpMessagingTemplate messagingTemplate;

    private final MessageRepository messageRepository;

    private final IDtoMapper<MessageDTO, MessageEntity, MessageDTO> messageMapper;

    // Mapa para registrar los usuarios conectados
    private final Map<String, Boolean> connectedUsers = new ConcurrentHashMap<>();


    @Override
    public ResponseEntity<List<MessageDTO>> getMessagesByUserId(String userId) {
        List<MessageEntity> messages = messageRepository.findAllByUserId(userId);
        if(messages.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        List<MessageDTO> messageDTOs = messages.stream()
                .map(messageMapper::toDto)
                .toList();
        return ResponseEntity.ok(messageDTOs);
    }

    @Override
    public ResponseEntity<MessageDTO> changeMessageState(Long messageId) {
        MessageEntity message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        message.setState(MessageState.READ);
        messageRepository.save(message);
        MessageDTO messageDTO = messageMapper.toDto(message);
        return ResponseEntity.ok(messageDTO);
    }

    @Override
    public ResponseEntity<?> deleteMessage(Long messageId) {
        MessageEntity message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        messageRepository.deleteById(messageId);
        return ResponseEntity.ok().build();
    }

    @Override
    public void sendNotification(MessageEvent event) {

        // Envía tanto al topic general como al destino específico del usuario
        messagingTemplate.convertAndSend("/topic/notifications/" + event.userId(), event.message());

        // También puedes usar el destino específico de usuario si lo prefieres
        // messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", message);
    }


    // Método para registrar conexiones de usuarios
    public void registerUserConnection(String userId, boolean connected) {
        if (userId != null) {
            connectedUsers.put(userId, connected);
            log.info("Usuario {} {}", userId, connected ? "conectado" : "desconectado");
        } else {
            log.warn("Intento de registrar conexión con userId nulo");
        }
    }

    // Método para verificar si un usuario está conectado
    @Override
    public boolean isUserConnected(String userId) {
        return connectedUsers.getOrDefault(userId, false);
    }
}