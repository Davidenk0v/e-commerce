package com.ecommerce.messaging.dtos;


import com.ecommerce.messaging.dtos.enums.EConnectionStatus;


public record UserConnectionMessage(String userId, EConnectionStatus status) {
}
