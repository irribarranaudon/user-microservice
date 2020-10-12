package com.irribarra.microservice.app.usermicroservice.service.impl;

import com.irribarra.microservice.app.usermicroservice.exception.BusinessException;
import com.irribarra.microservice.app.usermicroservice.models.dto.UserCreateResponseDTO;
import com.irribarra.microservice.app.usermicroservice.models.dto.UserResponseDTO;
import com.irribarra.microservice.app.usermicroservice.models.entity.User;
import com.irribarra.microservice.app.usermicroservice.models.repository.UserRepository;
import com.irribarra.microservice.app.usermicroservice.service.UserService;
import com.irribarra.microservice.app.usermicroservice.util.ExceptionUtil;
import com.irribarra.microservice.app.usermicroservice.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Clase encargada de manejar la lógica para la creación, edición, eliminación y actualización de usuarios.
 *
 * @author Patricio Irribarra
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String EMAIL_EXIST_ERROR = "El correo ya se encuentra registrado.";
    private static final String USER_NOT_FOUND = "Usuario no encontrado.";

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return UserUtils.getUserResponse(users);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return email != null ? userRepository.findByEmail(email) : Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> findById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.flatMap(value -> UserUtils.getUserResponse(Collections.singletonList(value)).stream().findFirst());

    }

    @Override
    @Transactional
    public UserCreateResponseDTO saveOrUpdate(User user, UUID id) throws BusinessException {
        UserUtils.validateEmailFormatt(user.getEmail());
        return getResponseUser(id != null ? updateUser(user, id) : saveUser(user));
    }

    /**
     * @param user entidad usuario para crear o actualizar.
     * @return respuesta custom.
     */
    private UserCreateResponseDTO getResponseUser(User user) {
        UserCreateResponseDTO response = new UserCreateResponseDTO();
        try {
            BeanUtils.copyProperties(response, user);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Error al copiar propiedades de la respuesta.");
            throw new RuntimeException();
        }
        return response;
    }

    private User updateUser(User user, UUID id) throws BusinessException {

        Optional<User> userByEmail = findByEmail(user.getEmail());
        ExceptionUtil.throwExecIf(userByEmail.isPresent() && !userByEmail.get().getId().equals(id), EMAIL_EXIST_ERROR, HttpStatus.CONFLICT);

        Optional<User> oUser = userRepository.findById(id);
        ExceptionUtil.throwExecIf(!oUser.isPresent(), USER_NOT_FOUND, HttpStatus.NOT_FOUND);

        log.info("Actualizando usuario.");
        User userToUpdate = oUser.get();
        userToUpdate.setEmail(user.getEmail() != null ? user.getEmail() : userToUpdate.getEmail());
        userToUpdate.setIsActive(user.getIsActive());
        userToUpdate.setModified(LocalDateTime.now());
        userToUpdate.setName(user.getName());
        return userRepository.save(userToUpdate);
    }

    private User saveUser(User user) throws BusinessException {
        Optional<User> userByEmail = findByEmail(user.getEmail());
        ExceptionUtil.throwExecIf(userByEmail.isPresent(), EMAIL_EXIST_ERROR, HttpStatus.CONFLICT);
        UserUtils.validatePasswordFormatt(user.getPassword());
        UserUtils.enryptPassword(user);
        UserUtils.generateToken(user);
        user.setIsActive(true);
        log.info("Guardando usuario.");
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }
}
