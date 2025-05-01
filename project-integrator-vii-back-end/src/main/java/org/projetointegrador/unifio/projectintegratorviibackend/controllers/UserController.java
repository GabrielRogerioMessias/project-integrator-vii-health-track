package org.projetointegrador.unifio.projectintegratorviibackend.controllers;


import org.projetointegrador.unifio.projectintegratorviibackend.models.User;
import org.projetointegrador.unifio.projectintegratorviibackend.models.security.AccountCredentialsDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.security.ForgetPasswordDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.security.ResetPasswordRequest;
import org.projetointegrador.unifio.projectintegratorviibackend.models.security.TokenDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.security.jwt.EmailTokenUtil;
import org.projetointegrador.unifio.projectintegratorviibackend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        TokenDTO tokenResponse = userService.signIn(loginData);
        return ResponseEntity.ok().body(tokenResponse);

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

    @PostMapping("/send-email-forget-password")
    public ResponseEntity<?> sendForgetPassword(@RequestBody ForgetPasswordDTO email) {
        userService.forgetPassword(email);
        return ResponseEntity.status(HttpStatus.CREATED).body("Please check your inbox, and follow the instructions;");
    }

    @GetMapping(value = "verify-forget")
    public ResponseEntity<?> createNewPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Password successfully verified.");
    }
}
