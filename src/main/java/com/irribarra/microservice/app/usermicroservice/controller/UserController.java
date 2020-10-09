package com.irribarra.microservice.app.usermicroservice.controller;

import com.irribarra.microservice.app.usermicroservice.models.entity.User;
import com.irribarra.microservice.app.usermicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        User newUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> saveUser(@RequestBody User user, @PathVariable UUID id) {
        Optional<User> oUser = userService.findById(id);
        if (oUser.isPresent()) {
            User userToUpdate = oUser.get();
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setIsActive(user.getIsActive());
            userToUpdate.setModified(LocalDate.now());
            userToUpdate.setName(user.getName());
            userService.save(userToUpdate);
            return ResponseEntity.status(HttpStatus.CREATED).body(userToUpdate);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable UUID id) {
        Optional<User> userOptional = userService.findById(id);
        userOptional.ifPresent(user -> ResponseEntity.ok().body(user));
        return ResponseEntity.notFound().build();
    }
}
