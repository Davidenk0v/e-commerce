package com.ecommerce.product.dto.response;

import com.ecommerce.product.entity.MessageType;

public record MessageEvent(String userId, Long ownerId, String message, String timestamp, MessageType type) {
}
