package com.irribarra.microservice.app.usermicroservice.service.impl;

import com.irribarra.microservice.app.usermicroservice.models.entity.User;
import com.irribarra.microservice.app.usermicroservice.models.repository.UserRepository;
import com.irribarra.microservice.app.usermicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Clase encargada de manejar la lógica para la creación, edición, eliminación y actualización de usuarios.
 *
 * @author Patricio Irribarra
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User saveOrUpdate(User user, UUID id) {
        if (id != null) {
            Optional<User> oUser = findById(id);
            if (!oUser.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.");
            }
            User userToUpdate = oUser.get();
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setIsActive(user.getIsActive());
            userToUpdate.setModified(LocalDateTime.now());
            userToUpdate.setName(user.getName());
        }
        Optional<User> userByEmail = findByEmail(user.getEmail());
        if(userByEmail.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email ya se encuentra registrado.");
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }
}
