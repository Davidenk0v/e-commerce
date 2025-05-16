package com.ecommerce.messaging.dtos;

import com.ecommerce.messaging.dtos.enums.MessageState;
import com.ecommerce.messaging.dtos.enums.MessageType;
import lombok.Builder;

@Builder
public record MessageDTO(
        Long id,
        String userId,
        Long ownerId,
        String message,
        MessageType type,
        MessageState state,
        String date
) {
}
