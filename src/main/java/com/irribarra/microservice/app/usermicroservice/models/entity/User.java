package com.irribarra.microservice.app.usermicroservice.models.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users",
        indexes = @Index(
                name = "idx_user_email",
                columnList = "email",
                unique = true
        ))
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
    @Column(unique = true)
    private String email;
    private String password;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    private LocalDateTime modified;
    private LocalDateTime created;
    private String token;
    @Column(name = "is_active")
    private Boolean isActive;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.created = now;
        this.modified = now;
        this.lastLogin = now;
        this.isActive = true;
    }

}
