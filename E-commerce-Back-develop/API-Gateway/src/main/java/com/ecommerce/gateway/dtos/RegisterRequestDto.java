package com.ecommerce.gateway.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto{

    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private List<String> role;
    private String password;
    private String idKeycloak;
}

