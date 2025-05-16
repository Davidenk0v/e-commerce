package com.ecommerce.gateway.controllers;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.gateway.dtos.LoginRequestDto;
import com.ecommerce.gateway.dtos.RegisterRequestDto;
import com.ecommerce.gateway.services.KeycloakService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*"
)
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final KeycloakService keycloakService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        return keycloakService.loginUser(loginRequest);
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return keycloakService.createUser(registerRequestDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        return keycloakService.refreshToken(refreshToken);
    }


    @GetMapping("/logout/{idUser}")
    public ResponseEntity<?> logout(@PathVariable String idUser) {
        return keycloakService.logoutUser(idUser);
    }

}
