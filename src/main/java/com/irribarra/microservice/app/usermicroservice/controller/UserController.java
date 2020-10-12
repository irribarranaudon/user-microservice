package com.irribarra.microservice.app.usermicroservice.controller;

import com.irribarra.microservice.app.usermicroservice.exception.BusinessException;
import com.irribarra.microservice.app.usermicroservice.models.dto.UserCreateResponseDTO;
import com.irribarra.microservice.app.usermicroservice.models.dto.UserResponseDTO;
import com.irribarra.microservice.app.usermicroservice.models.entity.User;
import com.irribarra.microservice.app.usermicroservice.service.UserService;
import com.irribarra.microservice.app.usermicroservice.util.ExceptionUtil;
import com.irribarra.microservice.app.usermicroservice.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<?> saveUser(@RequestBody User user) throws BusinessException {
        log.info("method - saveUser");
        UserUtils.validateUserRequestParameters(user);
        UserCreateResponseDTO newUser = userService.saveOrUpdate(user, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable UUID id) throws BusinessException {
        log.info("method - updateUser");
        UserCreateResponseDTO updatedUser = userService.saveOrUpdate(user, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedUser);
    }

    @GetMapping()
    public ResponseEntity<?> getUsers() {
        log.info("method - getUsers");
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable UUID id) throws BusinessException {
        log.info("method - getUser");
        Optional<UserResponseDTO> userOptional = userService.findById(id);
        ExceptionUtil.throwExecIf(!userOptional.isPresent(), "Usuario no encontrado.", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok().body(userOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        log.info("method - deleteUser");
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuario eliminado correctamente.");
        userService.deleteById(id);
        return ResponseEntity.ok(response);
    }
}
