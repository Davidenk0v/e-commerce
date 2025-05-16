package com.ecommerce.product.controller;

import com.ecommerce.product.dto.response.MessageEvent;
import com.ecommerce.product.entity.MessageType;
import com.ecommerce.product.service.impl.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product/messages")
@RequiredArgsConstructor
public class KafkaControllerTest {
    private final KafkaService kafkaService;

//    @PostMapping("/product")
//    public ResponseEntity<?> sendProductMessage(@RequestParam String userId, @RequestParam String message) {
//        try {
//            MessageEvent messageEvent = new MessageEvent(userId,ownerId, message, String.valueOf(System.currentTimeMillis()), MessageType.QUESTION);
//            kafkaService.sendProductMessage(message, userId);
//            return ResponseEntity.ok().body("Mensaje de producto enviado con éxito");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error al enviar mensaje de producto: " + e.getMessage());
//        }
//    }

//    @PostMapping("/cart")
//    public ResponseEntity<?> sendCartMessage(@RequestParam String userId, @RequestParam String message) {
//        try {
//            kafkaService.sendCartMessage(message, userId);
//            return ResponseEntity.ok().body("Mensaje de orden enviado con éxito");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error al enviar mensaje de orden: " + e.getMessage());
//        }
//    }
//
//    @PostMapping("/inventory")
//    public ResponseEntity<?> sendInventoryMessage(@RequestParam String userId, @RequestParam String message) {
//        try {
//            kafkaService.sendInventoryMessage(message, userId);
//            return ResponseEntity.ok().body("Mensaje de usuario enviado con éxito");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error al enviar mensaje de usuario: " + e.getMessage());
//        }
//    }
}
