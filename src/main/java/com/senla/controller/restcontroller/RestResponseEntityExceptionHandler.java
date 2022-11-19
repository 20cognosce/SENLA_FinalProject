package com.senla.controller.restcontroller;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.controller.customexception.IllegalRoleForActionException;
import com.senla.controller.customexception.IncorrectAgeException;
import com.senla.controller.customexception.IncorrectLoginException;
import com.senla.controller.customexception.IncorrectPasswordException;
import com.senla.controller.customexception.IncorrectPhoneException;
import com.senla.controller.customexception.LoginAlreadyExistException;
import com.senla.controller.customexception.PhoneAlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            EntityNotFoundByIdException.class,
            IllegalRoleForActionException.class,
            IncorrectAgeException.class,
            IncorrectLoginException.class,
            IncorrectPasswordException.class,
            IncorrectPhoneException.class,
            LoginAlreadyExistException.class,
            PhoneAlreadyExistException.class})
    protected ResponseEntity<Object> handleExpectedException(Exception ex, WebRequest req) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String responseBody = ex.getMessage();
        log.warn(responseBody, ex);
        return handleExceptionInternal(ex, responseBody, httpHeaders, HttpStatus.I_AM_A_TEAPOT, req);
    }

    //TODO: refactor
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest req) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String responseBody = ex.getMessage();
        log.error(responseBody, ex);
        return handleExceptionInternal(ex, responseBody, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR, req);
    }
}
