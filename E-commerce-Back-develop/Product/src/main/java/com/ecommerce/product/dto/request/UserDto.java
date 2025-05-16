package com.ecommerce.product.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record UserDto(
        String idUser,

        String email,

        String lastname,

        String firstname,

        String username,

        List<String>role

) {
}
