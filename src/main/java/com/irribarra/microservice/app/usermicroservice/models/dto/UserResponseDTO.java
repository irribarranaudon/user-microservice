package com.irribarra.microservice.app.usermicroservice.models.dto;

import com.irribarra.microservice.app.usermicroservice.models.entity.Phone;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Formato retornado al cliente al crear un nuevo user.
 */
@Data
@Builder
public class UserResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private LocalDateTime lastLogin;
    private LocalDateTime modified;
    private LocalDateTime created;
    private Boolean isActive;
    private List<Phone> phones;
}
