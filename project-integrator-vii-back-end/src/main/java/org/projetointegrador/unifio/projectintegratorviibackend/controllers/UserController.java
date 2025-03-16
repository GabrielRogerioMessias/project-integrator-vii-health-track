package org.projetointegrador.unifio.projectintegratorviibackend.controllers;

import org.projetointegrador.unifio.projectintegratorviibackend.config.SecurityConfig;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.securityDTO.AccountCredentialsDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.securityDTO.TokenDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.security.exceptions.CustomBadCredentialsException;
import org.projetointegrador.unifio.projectintegratorviibackend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> singIn(@RequestBody AccountCredentialsDTO loginData) {
        if (userService.checkIfParamsIsNotNull(loginData)) {
            return ResponseEntity.ok().body("Invalid Client Request: Email or Username is null or blank," +
                    " please consult the API documentation");
        }
        try {
            TokenDTO tokenResponse = userService.signIn(loginData);
            if (tokenResponse == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client Request");
            }
            return ResponseEntity.ok().body(tokenResponse);
        } catch (BadCredentialsException e) {
            throw new CustomBadCredentialsException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
