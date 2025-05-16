package com.ecommerce.gateway.dtos;


import java.util.List;


import lombok.*;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    private String idUser;

    private String email;

    private String lastname;

    private String firstname;

    private String username;

    private String password;

    private List<String> role;

    private String urlImg;
}
