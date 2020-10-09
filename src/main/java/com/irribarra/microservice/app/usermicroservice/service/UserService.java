package com.irribarra.microservice.app.usermicroservice.service;

import com.irribarra.microservice.app.usermicroservice.models.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<User> findAll();

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    User saveOrUpdate(User user, UUID id);

    void deleteById(UUID id);

}
