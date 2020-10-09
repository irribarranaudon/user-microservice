package com.irribarra.microservice.app.usermicroservice.service;

import com.irribarra.microservice.app.usermicroservice.models.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(UUID id);

    User save(User user);

    void deleteById(UUID id);

}
