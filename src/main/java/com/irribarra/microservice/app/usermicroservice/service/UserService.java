package com.irribarra.microservice.app.usermicroservice.service;

import com.irribarra.microservice.app.usermicroservice.exception.BusinessException;
import com.irribarra.microservice.app.usermicroservice.models.dto.UserCreateResponseDTO;
import com.irribarra.microservice.app.usermicroservice.models.dto.UserResponseDTO;
import com.irribarra.microservice.app.usermicroservice.models.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<UserResponseDTO> findAll();

    Optional<User> findByEmail(String email);

    Optional<UserResponseDTO> findById(UUID id);

    UserCreateResponseDTO saveOrUpdate(User user, UUID id) throws BusinessException;

    void deleteById(UUID id);

}
