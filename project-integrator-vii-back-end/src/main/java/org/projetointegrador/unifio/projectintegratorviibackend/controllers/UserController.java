package org.projetointegrador.unifio.projectintegratorviibackend.controllers;

import org.projetointegrador.unifio.projectintegratorviibackend.config.SecurityConfig;
import org.projetointegrador.unifio.projectintegratorviibackend.models.User;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.securityDTO.AccountCredentialsDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.securityDTO.TokenDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.security.exceptions.CustomBadCredentialsException;
import org.projetointegrador.unifio.projectintegratorviibackend.security.jwt.EmailTokenUtil;
import org.projetointegrador.unifio.projectintegratorviibackend.services.EmailService;
import org.projetointegrador.unifio.projectintegratorviibackend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class UserController {
    private final UserService userService;
    private final EmailTokenUtil tokenUtil;

    public UserController(UserService userService, EmailTokenUtil tokenUtil) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
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

    @GetMapping(value = "/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        String emailString = tokenUtil.extractEmail(token);
        User user = userService.getUserByEmail(emailString);
        if (user == null || user.getVerificationToken() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your Token Verification Email Was Expired!");
        }
        if (!tokenUtil.validateEmailToken(token) || !user.getVerificationToken().equals(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your Token Verification Email Was Expired!");
        }
        userService.validateEmail(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Email successfully verified!");
    }
}
