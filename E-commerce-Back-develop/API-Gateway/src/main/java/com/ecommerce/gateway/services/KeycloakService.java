package com.ecommerce.gateway.services;


import com.ecommerce.gateway.dtos.LoginRequestDto;
import com.ecommerce.gateway.dtos.RegisterRequestDto;
import com.ecommerce.gateway.dtos.UserDTO;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.jwt.Jwt;

public interface KeycloakService {
    ResponseEntity<?> findAllUsers();

    ResponseEntity<?> loginUser(LoginRequestDto loginRequest);

    ResponseEntity<?> logoutUser(String idUser);

    ResponseEntity<AccessTokenResponse> refreshToken(String refreshToken);

    ResponseEntity<?> searchUserByUsername(String username);

    ResponseEntity<?> searchUserById(String userId);

    UserRepresentation findUserById(String userId);

    UserRepresentation findUserByEmail(String email);

    ResponseEntity<?> createUser(@NonNull RegisterRequestDto userDTO);

    ResponseEntity<?> deleteUser(String userId);

    ResponseEntity<?> updateUser(String userId, @NonNull UserDTO userDTO);

    ResponseEntity<UserDTO> getCurrentUser(Jwt jwt);

}
