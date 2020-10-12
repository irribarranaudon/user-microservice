package com.irribarra.microservice.app.usermicroservice.util;

import com.irribarra.microservice.app.usermicroservice.exception.BusinessException;
import com.irribarra.microservice.app.usermicroservice.models.dto.UserResponseDTO;
import com.irribarra.microservice.app.usermicroservice.models.entity.Phone;
import com.irribarra.microservice.app.usermicroservice.models.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Clase destinada a validaciones y generación de campos de usuario.
 */
@Slf4j
public class UserUtils {

    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";
    private static final String PASSWORD_NUMBER_REGEX = "^\\D*(\\d)\\D*(\\d)\\D*$";
    private static final String PASSWORD_CAPITAL_MAX_REGEX = "^.*[A-Z].*[A-Z].*$";
    private static final String PASSWORD_CAPITAL_MIN_REGEX = "^(?=.*[A-Z]).*$";
    private static final String PASSWORD_LOWER_REGEX = "(?=.*[a-z]).*$";
    private static final String PASSWORD_LENGTH_REGEX = "(?=.{8,}$).*$";

    private static final String VALIDATE_ERROR_MESSAGE = "%s es un campo obligatorio.";
    private static final String ERROR_MESSAGE_PASS_NUMBER = "La contraseña debe contener 2 números.";
    private static final String ERROR_MESSAGE_PASS_CAPITAL_MAX = "La contraseña debe contener solo 1 mayúscula.";
    private static final String ERROR_MESSAGE_PASS_CAPITAL_MIN = "La contraseña debe contener al menos una mayúscula.";
    private static final String ERROR_MESSAGE_PASS_LOWER = "La contraseña debe contener al menos una minúscula,";
    private static final String ERROR_MESSAGE_PASS_LENGTH = "La contraseña debe tener mínimo 8 carácteres.";
    private static final String ERROR_MESSAGE_PASS_BLAK_SPACES = "La contraseña no puede contener espacios en blanco";

    /**
     * Valida que el formato del email ingresado sea el correcto.
     *
     * @param email correo de user
     * @throws BusinessException excepcion custom
     */
    public static void validateEmailFormatt(String email) throws BusinessException {
        if (StringUtils.isEmpty(email)) {
            return;
        }
        log.info("Validando formato de correo ingresado: {}", email);
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        ExceptionUtil.throwExecIf(!matcher.matches(), "El correo no tiene el formato correcto.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Valida que la contraseña cumpla con el formato según el negocio.
     *
     * @param password contraseña de usuario
     */
    public static void validatePasswordFormatt(String password) throws BusinessException {
        log.info("Validando formato de contraseña ingresada.");
        patternValidator(Pattern.compile(PASSWORD_NUMBER_REGEX), password, ERROR_MESSAGE_PASS_NUMBER, false);
        patternValidator(Pattern.compile(PASSWORD_CAPITAL_MAX_REGEX), password, ERROR_MESSAGE_PASS_CAPITAL_MAX, true);
        patternValidator(Pattern.compile(PASSWORD_CAPITAL_MIN_REGEX), password, ERROR_MESSAGE_PASS_CAPITAL_MIN, false);
        patternValidator(Pattern.compile(PASSWORD_LOWER_REGEX), password, ERROR_MESSAGE_PASS_LOWER, false);
        patternValidator(Pattern.compile(PASSWORD_LENGTH_REGEX), password, ERROR_MESSAGE_PASS_LENGTH, false);
        ExceptionUtil.throwExecIf(password.contains(" "), ERROR_MESSAGE_PASS_BLAK_SPACES, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Validador de expresiones regulares.
     *
     * @param pattern Patron con regex
     * @param pass    contraseña a validar
     * @param message mensaje informativo
     * @param valid   si es true retorna error cuando matcher retorne true, si es false retorna erroe cuando matches es false.
     * @throws BusinessException excepcion de negocio
     */
    private static void patternValidator(Pattern pattern, String pass, String message, Boolean valid) throws BusinessException {
        Matcher matcher = pattern.matcher(pass);
        ExceptionUtil.throwExecIf(valid == matcher.matches(), message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Encripta la contraseña del usuario para no persistir información sensible.
     *
     * @param user entidad usuario
     */
    public static void enryptPassword(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        if (user.getPassword() != null) {
            log.info("Encriptando contraseña de usuario: {}", user.getEmail());
            user.setPassword(encoder.encode(user.getPassword()));
        }
    }

    /**
     * Valida que los campos obligatorios estén presentes en la solicitud.
     *
     * @param user entidad usuario
     * @throws BusinessException excepcion custom
     */
    public static void validateUserRequestParameters(User user) throws BusinessException {
        log.info("Validando datos de usuario: {}", user.getEmail());
        ExceptionUtil.throwExecIf(user.getPassword() == null, String.format(VALIDATE_ERROR_MESSAGE, "password"));
        ExceptionUtil.throwExecIf(user.getEmail() == null, String.format(VALIDATE_ERROR_MESSAGE, "email"));
        ExceptionUtil.throwExecIf(user.getName() == null, String.format(VALIDATE_ERROR_MESSAGE, "name"));
        if (!user.getPhones().isEmpty()) {
            for (Phone phone : user.getPhones()) {
                log.info("Validando telefonos ingresados.");
                ExceptionUtil.throwExecIf(phone.getCityCode() == null, String.format(VALIDATE_ERROR_MESSAGE, "city code"));
                ExceptionUtil.throwExecIf(phone.getCountryCode() == null, String.format(VALIDATE_ERROR_MESSAGE, "country code"));
                ExceptionUtil.throwExecIf(phone.getNumber() == null, String.format(VALIDATE_ERROR_MESSAGE, "number"));
            }
        }
    }

    /**
     * Método útil para la generación de tokens de usuario.
     */
    public static void generateToken(User user) {
        log.info("Generando token para user: {}", user.getEmail());
        user.setToken(UUID.randomUUID().toString());
    }

    /**
     * Convierte una lista de entidades usuario, en una lista de una respuesta custom con valores no sensibles.
     *
     * @param users Lista de usuarios
     * @return Lista de usuarios custom.
     */
    public static List<UserResponseDTO> getUserResponse(List<User> users) {
        return users.stream().map(user ->
                UserResponseDTO.builder()
                        .created(user.getCreated())
                        .email(user.getEmail())
                        .id(user.getId())
                        .isActive(user.getIsActive())
                        .lastLogin(user.getLastLogin())
                        .modified(user.getModified())
                        .name(user.getName())
                        .phones(user.getPhones())
                        .build()
        ).collect(Collectors.toList());
    }
}
