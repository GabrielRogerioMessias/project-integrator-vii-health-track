package org.projetointegrador.unifio.projectintegratorviibackend.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.projetointegrador.unifio.projectintegratorviibackend.controllers.exceptions.StandardError;
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
@Tag(name = "Authenticação", description = "Permite o envio do e-mail de verificação, esqueci minha senha, criar uma nova senha e o login para gerar o token jwt")
public class UserController {
    private final UserService userService;
    private final EmailTokenUtil tokenUtil;

    public UserController(UserService userService, EmailTokenUtil tokenUtil) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
    }

    @Operation(
            summary = "Efetuar Login",
            description = "Permite o fazer login, e gerar o token JWT",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso", content = @Content(schema = @Schema(implementation = TokenDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Usuário ou senha inválidos", content = @Content(schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "400", description = "Erro ao efetuar o login", content = @Content(schema = @Schema(implementation = StandardError.class)))
            }
    )
    @PostMapping(value = "/login")
    public ResponseEntity<?> singIn(@RequestBody AccountCredentialsDTO loginData) {
        TokenDTO tokenResponse = userService.signIn(loginData);
        return ResponseEntity.ok().body(tokenResponse);

    }

    @Operation(
            summary = "Verificar e-mail",
            description = "Verificar e-mail de usuário não verificados",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Email validade com sucesso", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Token inválido ou expirado", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
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

    @Operation(
            summary = "Enviar e-mail esqueci minha senha",
            description = "Um e-mail de esqueci minha senha é enviado",
            responses = {
                    @ApiResponse(responseCode = "201", description = "O e-mail de redefinição é enviado com sucesso", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/send-email-forget-password")
    public ResponseEntity<?> sendForgetPassword(@RequestBody ForgetPasswordDTO email) {
        userService.forgetPassword(email);
        return ResponseEntity.status(HttpStatus.CREATED).body("Please check your inbox, and follow the instructions;");
    }

    @Operation(
            summary = "Criar nova senha",
            description = "Permite o usuário a criar uma nova senha, passando o token enviado no e-mail e a nova senha",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Senha criada com sucesso", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Erro ao criar uma nova senha", content = @Content(schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "401", description = "Token de redefinição expirado", content = @Content(schema = @Schema(implementation = StandardError.class))),
            }
    )
    @GetMapping(value = "verify-forget")
    public ResponseEntity<?> createNewPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Password successfully verified.");
    }
}
