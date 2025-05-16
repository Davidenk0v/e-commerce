package com.ecommerce.gateway.mapper;

import com.ecommerce.gateway.dtos.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;

public class UserMapper {

    public static UserDTO userRepresentationToUserDTO(UserRepresentation user) {
        return UserDTO.builder()
                .idUser(user.getId())
                .email(user.getEmail())
                .lastname(user.getLastName())
                .firstname(user.getFirstName())
                .username(user.getUsername())
                .role(null)
                .urlImg(null)
                .build();
    }
}
