package org.projetointegrador.unifio.projectintegratorviibackend.controllers.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.projetointegrador.unifio.projectintegratorviibackend.security.exceptions.CustomBadCredentialsException;
import org.projetointegrador.unifio.projectintegratorviibackend.security.exceptions.InvalidJwtAuthenticationException;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.NullEntityFieldException;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.UserAlreadyRegistered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<StandardError> userAlreadyRegistered(UserAlreadyRegistered e, HttpServletRequest request) {
        String error = "User already registered";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public ResponseEntity<StandardError> cannotScheduleException(InvalidJwtAuthenticationException e, HttpServletRequest request) {
        String error = "Invalid JWT Authentication";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }
    @ExceptionHandler(CustomBadCredentialsException.class)
    public ResponseEntity<StandardError> cannotScheduleException(CustomBadCredentialsException e, HttpServletRequest request) {
        String error = "Username or Password Invalid";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(NullEntityFieldException.class)
    public ResponseEntity<StandardError> fieldsNotBeNullOrEmpty(NullEntityFieldException e, HttpServletRequest request) {
        String error = "Fields cannot be null or empty";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
