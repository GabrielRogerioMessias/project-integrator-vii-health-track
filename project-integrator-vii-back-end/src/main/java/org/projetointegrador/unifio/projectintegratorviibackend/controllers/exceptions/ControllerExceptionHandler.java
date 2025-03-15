package org.projetointegrador.unifio.projectintegratorviibackend.controllers.exceptions;

import jakarta.servlet.http.HttpServletRequest;
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
}
