package com.irribarra.microservice.app.usermicroservice.util;

import com.irribarra.microservice.app.usermicroservice.exception.BusinessException;
import com.irribarra.microservice.app.usermicroservice.models.dto.UserResponseDTO;
import com.irribarra.microservice.app.usermicroservice.models.entity.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserUtilsTest {

    @Test
    void validateEmailFormatt() {
        Exception exception = assertThrows(BusinessException.class, () ->
                UserUtils.validateEmailFormatt("test@@gmail.com"));

        String expectedMessage = "El correo no tiene el formato correcto";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validatePasswordFormatt() {
        Exception exceptionNumber = assertThrows(BusinessException.class, () ->
                UserUtils.validatePasswordFormatt("passwordtesetL"));
        Exception exceptionCapital = assertThrows(BusinessException.class, () ->
                UserUtils.validatePasswordFormatt("passwordt33esetLL"));
        Exception exceptionLower = assertThrows(BusinessException.class, () ->
                UserUtils.validatePasswordFormatt("..........33L"));
        Exception exceptionSpaces = assertThrows(BusinessException.class, () ->
                UserUtils.validatePasswordFormatt("pass23wordt esetL"));
        Exception exceptionLength = assertThrows(BusinessException.class, () ->
                UserUtils.validatePasswordFormatt("pa22L"));

        assertNotNull(exceptionNumber);
        assertNotNull(exceptionCapital);
        assertNotNull(exceptionLower);
        assertNotNull(exceptionSpaces);
        assertNotNull(exceptionLength);
    }

    @Test
    void enryptPassword() {
        String passTest = "passtest";
        User user = new User();
        user.setPassword(passTest);
        user.setEmail("testUser@gmail.com");

        UserUtils.enryptPassword(user);

        assertNotEquals(passTest, user.getPassword());
    }

    @Test
    void validateUserRequestParameters() {
        User user = new User();
        user.setEmail("test");
        user.setPassword("test");
        Exception exceptionName = assertThrows(BusinessException.class, () ->
                UserUtils.validateUserRequestParameters(user));

        assertNotNull(exceptionName);

        user.setName("test");
        user.setPassword(null);
        Exception exceptionPassword = assertThrows(BusinessException.class, () ->
                UserUtils.validateUserRequestParameters(user));

        assertNotNull(exceptionPassword);

        user.setPassword("test");
        user.setEmail(null);

        Exception exceptionEmail = assertThrows(BusinessException.class, () ->
                UserUtils.validateUserRequestParameters(user));

        assertNotNull(exceptionEmail);
    }

    @Test
    void generateToken() {

        String passTest = "passtest";
        User user = new User();
        user.setPassword(passTest);
        user.setEmail("testUser@gmail.com");

        UserUtils.generateToken(user);

        assertNotNull(user.getToken());
    }

    @Test
    void getUserResponse() {
        User user = new User();
        user.setEmail("testUser@gmail.com");

        UserResponseDTO responseDTO = UserResponseDTO.builder()
                .email("testUser@gmail.com")
                .phones(new ArrayList<>())
                .build();

        List<UserResponseDTO> list = new ArrayList<>();
        list.add(responseDTO);

        assertEquals(list, UserUtils.getUserResponse(Collections.singletonList(user)));

    }
}