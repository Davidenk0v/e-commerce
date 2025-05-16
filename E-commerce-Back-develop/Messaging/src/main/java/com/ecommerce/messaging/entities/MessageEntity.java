package com.ecommerce.messaging.entities;

import com.ecommerce.messaging.dtos.enums.MessageState;
import com.ecommerce.messaging.dtos.enums.MessageType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String userId;

    @NotNull
    private Long ownerId;

    @NotBlank
    private String message;

    @CreationTimestamp
    private Instant timestamp;

    @NotNull
    private MessageType type;

    @NotNull
    private MessageState state;


}
