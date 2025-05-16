package com.ecommerce.gateway.controllers;


import com.ecommerce.gateway.dtos.RegisterRequestDto;
import com.ecommerce.gateway.dtos.UserDTO;
import com.ecommerce.gateway.services.KeycloakServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/keycloak/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class KeycloakController {

    private final KeycloakServiceImpl keycloakService;

    @GetMapping("/search")
    public ResponseEntity<?> findAllUsers() {
        return ResponseEntity.ok(keycloakService.findAllUsers());
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<?> searchUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(keycloakService.searchUserByUsername(username));
    }

    @GetMapping("/find-by-id/{idUser}")
    public ResponseEntity<UserDTO> searchUserById(@PathVariable String idUser) {
        return keycloakService.searchUserById(idUser);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequestDto userDTO) throws URISyntaxException {
        return keycloakService.createUser(userDTO);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO) {

        return keycloakService.updateUser(userId, userDTO);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        keycloakService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return keycloakService.getCurrentUser(jwt);
    }
}
