package ru.solarev.taskmanagementapi.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import ru.solarev.taskmanagementapi.exceptions.AccessDeniedException;
import ru.solarev.taskmanagementapi.exceptions.ExceptionBody;
import ru.solarev.taskmanagementapi.exceptions.ResourceMappingException;
import ru.solarev.taskmanagementapi.exceptions.ResourceNotFoundException;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handlerResourceNotFound(ResourceNotFoundException e) {
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(ResourceMappingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handlerResourceMapping(ResourceMappingException e) {
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerIllegalState(IllegalStateException e) {
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handlerAccessDenied() {
        return new ExceptionBody("Доступ запрещен");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleMethodArgumentNotValid(
            final MethodArgumentNotValidException e
    ) {
        ExceptionBody exceptionBody = new ExceptionBody("Ошибка валидации");
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        exceptionBody.setErrors(errors.stream()
                .collect(Collectors.toMap(FieldError::getField,
                        FieldError::getDefaultMessage)));
        return exceptionBody;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleConstraintViolation(
            final ConstraintViolationException e
    ) {
        ExceptionBody exceptionBody = new ExceptionBody("Ошибка валидации");
        exceptionBody.setErrors(e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage()
                )));
        return exceptionBody;
    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerAuthentication(AuthenticationException e) {
        return new ExceptionBody("Ошибка аутентификации");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handlerException(Exception e) {
        e.printStackTrace();
        return new ExceptionBody("Внутренняя ошибка сервера");
    }
}
