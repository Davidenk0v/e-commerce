package com.ecommerce.messaging.mappers.impl;

import com.ecommerce.messaging.dtos.MessageDTO;
import com.ecommerce.messaging.entities.MessageEntity;
import com.ecommerce.messaging.mappers.IDtoMapper;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class MessageMapper implements IDtoMapper<MessageDTO, MessageEntity, MessageDTO> {


    @Override
    public MessageEntity toEntity(MessageDTO dto) {
        return MessageEntity.builder()
                .message(dto.message())
                .ownerId(dto.ownerId())
                .type(dto.type())
                .state(dto.state())
                .userId(dto.userId())
                .build();
    }

    @Override
    public MessageDTO toDto(MessageEntity entity) {

        ZonedDateTime fecha = entity.getTimestamp().atZone(ZoneId.of("Atlantic/Canary"));

        // Define el formato con el locale en español
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Formatea
        String fechaFormateada = fecha.format(formatter);

        return MessageDTO.builder()
                .id(entity.getId())
                .message(entity.getMessage())
                .ownerId(entity.getOwnerId())
                .type(entity.getType())
                .state(entity.getState())
                .userId(entity.getUserId())
                .date(fechaFormateada)
                .build();
    }
}
