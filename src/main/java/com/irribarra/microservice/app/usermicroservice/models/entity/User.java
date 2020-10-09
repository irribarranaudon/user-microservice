package com.irribarra.microservice.app.usermicroservice.models.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private String email;
    private String password;
    @Column(name = "last_login")
    private LocalDate lastLogin;
    private LocalDate modified;
    private LocalDate created;
    private String token;
    @Column(name = "is_active")
    private Boolean isActive;

}
