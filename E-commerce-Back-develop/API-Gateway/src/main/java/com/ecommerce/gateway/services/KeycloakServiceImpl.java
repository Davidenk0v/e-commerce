package com.ecommerce.gateway.services;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.ecommerce.gateway.dtos.LoginRequestDto;
import com.ecommerce.gateway.dtos.RegisterRequestDto;
import com.ecommerce.gateway.dtos.UserDTO;
import com.ecommerce.gateway.keycloak.KeycloakProvider;
import com.ecommerce.gateway.mapper.UserMapper;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {
    
	private final KeycloakProvider keycloakProvider;

    public KeycloakServiceImpl(KeycloakProvider keycloakProvider) {
        this.keycloakProvider = keycloakProvider;
    }

    /**
     * Metodo para listar todos los usuarios de Keycloak
     *
     * @return List<UserRepresentation>
     */
    @Override
    public ResponseEntity<List<UserRepresentation>> findAllUsers() {
        try {
            List<UserRepresentation> userList = keycloakProvider.getRealmResource()
                    .users()
                    .list();
            if (userList.isEmpty()) {
                throw new NotFoundException("No se encontraron usuarios en el sistema");
            }
            return ResponseEntity.status(200).body(userList);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al buscar los usuarios");
        }
    }

    @Override
    public ResponseEntity<AccessTokenResponse> loginUser(LoginRequestDto loginRequest) {
        try {
            AccessTokenResponse token = keycloakProvider.getToken(loginRequest.username(), loginRequest.password());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            log.error("Error at login", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Override
    public ResponseEntity<AccessTokenResponse> refreshToken(String refreshToken) {
        try {
            AccessTokenResponse refreshedToken = keycloakProvider.refreshToken(refreshToken);
            return ResponseEntity.ok(refreshedToken);
        } catch (Exception e) {
            log.error("Error refreshing token", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Override
    public ResponseEntity<String> logoutUser(String idUser) {
        try {
            UserResource userResource = keycloakProvider.getUserResource().get(idUser);
            userResource.logout();
            return ResponseEntity.status(200).body("Se ha cerrado la sesión correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al cerrar la sesión");
        }

    }


    /**
     * Metodo para buscar un usuario por su username
     *
     * @return List<UserRepresentation>
     */
    @Override
    public ResponseEntity<List<UserRepresentation>> searchUserByUsername(String username) {
        try {
            List<UserRepresentation> userRepresentationList = keycloakProvider.getRealmResource().users()
                    .searchByUsername(username, true);
            return ResponseEntity.status(200).body(userRepresentationList);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("No se encontró el usuario con username: %s", username));
        }
    }

    /**
     * Metodo para buscar un usuario por su username
     *
     * @return List<UserRepresentation>
     */
    @Override
    public ResponseEntity<UserDTO> searchUserById(String userId) {
        try {
            UserRepresentation user;
            UserResource userResource = keycloakProvider.getRealmResource().users().get(userId);
            try {
            	user = userResource.toRepresentation();
            } catch (jakarta.ws.rs.NotFoundException e) {
                throw new NotFoundException("No se encontró el usuario con id: " + userId);
            }
            return ResponseEntity.status(200).body(UserMapper.userRepresentationToUserDTO(user));
        } catch (Exception e) {
            log.error("Error al buscar el usuario con id: {}", userId, e);
            throw new IllegalArgumentException(String.format("Error al buscar el usuario con id: %s", userId), e);
        }
    }

    @Override
    public UserRepresentation findUserById(String userId) {
        UserResource userResource = keycloakProvider.getRealmResource().users().get(userId);
        return userResource.toRepresentation();
    }

    @Override
    public UserRepresentation findUserByEmail(String email) {
        return keycloakProvider.getRealmResource().users()
                .search(email)
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Metodo para crear un usuario en keycloak
     *
     * @return String
     */
    @Override
    public ResponseEntity<HashMap<String, String>> createUser(@NonNull RegisterRequestDto userDTO) {

        int status = 0;
        UsersResource usersResource = keycloakProvider.getUserResource();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDTO.getFirstname());
        userRepresentation.setLastName(userDTO.getLastname());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setUsername(userDTO.getUsername());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        Response response = usersResource.create(userRepresentation);

        status = response.getStatus();

        if (status == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(userDTO.getPassword());

            usersResource.get(userId).resetPassword(credentialRepresentation);

            RealmResource realmResource = keycloakProvider.getRealmResource();
            List<RoleRepresentation> rolesRepresentation = null;

            if (userDTO.getRole() == null || userDTO.getRole().isEmpty()) {
                rolesRepresentation = List.of(realmResource.roles().get("user_e-commerce").toRepresentation());

            } else {
                rolesRepresentation = realmResource.roles()
                        .list()
                        .stream()
                        .filter(role -> userDTO.getRole()
                                .stream()
                                .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                        .toList();
            }
            realmResource.users().get(userId).roles().realmLevel().add(rolesRepresentation);
            HashMap<String, String> loginData = new HashMap<>();
            loginData.put("sub", userId);
            loginData.put("message", "Usuario creado correctamente");
            return ResponseEntity.status(201).body(loginData);

        } else if (status == 409) {
           throw new IllegalArgumentException("El usuario ya existe");
        } else {
            throw new IllegalArgumentException("Error al crear el usuario");
        }
    }

    /**
     * Metodo para borrar un usuario en keycloak
     *
     * @return void
     */
    @Override
    public ResponseEntity<String> deleteUser(String userId) {
        try {
        	keycloakProvider.getUserResource()
                    .get(userId)
                    .remove();
            return ResponseEntity.status(200).body("Usuario eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar el usuario");
        }
    }

    /**
     * Metodo para actualizar un usuario en keycloak
     *
     * @return void
     */
    @Override
    public ResponseEntity<String> updateUser(String userId, @NonNull UserDTO userDTO) {
        try {
            UserRepresentation user = new UserRepresentation();
            if (!Objects.equals(userDTO.getUsername(), "")) {
                user.setUsername(userDTO.getUsername());
            }
            if (!Objects.equals(userDTO.getLastname(), "")) {
                user.setLastName(userDTO.getLastname());
            }
            if (!Objects.equals(userDTO.getFirstname(), "")) {
                user.setFirstName(userDTO.getFirstname());
            }
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                credentialRepresentation.setTemporary(false);
                user.setEmailVerified(true);
                credentialRepresentation.setType(OAuth2Constants.PASSWORD);
                credentialRepresentation.setValue(userDTO.getPassword());
                user.setCredentials(Collections.singletonList(credentialRepresentation));
            }

            user.setEnabled(true);
            user.setEmailVerified(true);

            UserResource usersResource = keycloakProvider.getUserResource().get(userId);
            usersResource.update(user);

            return ResponseEntity.status(200).body("Usuario actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar el usuario");
        }
    }

    @Override
    public ResponseEntity<UserDTO> getCurrentUser(Jwt jwt) {
        if (jwt == null) {
            throw new IllegalArgumentException("Error al recibir el token");
        }

        String userId = jwt.getClaim("sub");
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = new ArrayList<>();

        if (realmAccess != null && realmAccess.containsKey("roles")) {
            Object rolesObj = realmAccess.get("roles");
            if (rolesObj instanceof List<?> rolesList) {
                for (Object role : rolesList) {
                    if (role instanceof String) {
                        roles.add((String) role);
                    }
                }
            }
        }

        UserRepresentation userRepresentation = findUserById(userId);
        if (userRepresentation == null) {
            throw new IllegalArgumentException("No se encontró el usuario");
        }

        UserDTO userDTO = UserMapper.userRepresentationToUserDTO(userRepresentation);
        userDTO.setRole(roles);

        return ResponseEntity.ok(userDTO);
    }
}
