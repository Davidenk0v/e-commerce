package com.ecommerce.messaging.dtos;

import com.ecommerce.messaging.dtos.enums.MessageType;

import java.time.Instant;

public record MessageEvent(String userId, Long ownerId, String message, Instant timestamp, MessageType type) {
}
