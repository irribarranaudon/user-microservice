package com.irribarra.microservice.app.usermicroservice.models.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Formato retornado al cliente al crear un nuevo user.
 */
@Data
public class UserCreateResponseDTO {
    private UUID id;
    private LocalDateTime lastLogin;
    private LocalDateTime modified;
    private LocalDateTime created;
    private String token;
    private Boolean isActive;

}
