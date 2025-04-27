package org.projetointegrador.unifio.projectintegratorviibackend.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.projetointegrador.unifio.projectintegratorviibackend.models.User;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.securityDTO.AccountCredentialsDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.securityDTO.ForgetPasswordDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.securityDTO.ResetPasswordRequest;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.securityDTO.TokenDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.UserRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.security.exceptions.CustomBadCredentialsException;
import org.projetointegrador.unifio.projectintegratorviibackend.security.exceptions.InvalidJwtAuthenticationException;
import org.projetointegrador.unifio.projectintegratorviibackend.security.jwt.EmailTokenUtil;
import org.projetointegrador.unifio.projectintegratorviibackend.security.jwt.JwtTokenProvider;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.NullEntityFieldException;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.UnverifiedEmailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final EmailTokenUtil emailTokenUtil;
    private final Validator validator;

    public UserService(UserRepository userRepository, JwtTokenProvider tokenProvider,
                       AuthenticationManager authenticationManager,
                       EmailService emailService, EmailTokenUtil emailTokenUtil,
                       Validator validator) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.emailTokenUtil = emailTokenUtil;
        this.validator = validator;
    }

    public void forgetPassword(ForgetPasswordDTO email) {
        List<String> errors;
        errors = validFields(email);
        if (!errors.isEmpty()) {
            throw new NullEntityFieldException(errors);
        }
        User user = userRepository.findByEmail(email.getEmail());
        if (user != null) {
            String forgetPasswordToken = EmailTokenUtil.generateForgetToken(email.getEmail());
            user.setResetToken(forgetPasswordToken);
            emailService.sendForgotPasswordEmail(email.getEmail(), forgetPasswordToken);
            userRepository.save(user);
        }
    }

    public void resetPassword(ResetPasswordRequest request) {
        List<String> errors;
        errors = validFields(request);
        if (!errors.isEmpty()) {
            throw new NullEntityFieldException(errors);
        }
        User user = userRepository.findByEmail(emailTokenUtil.extractEmail(request.getToken()));
        if (user == null) {
            throw new UsernameNotFoundException("Email not found, please, click forgot your password again, and fill with a valid email.");
        }
        if (!emailTokenUtil.validateEmailToken(request.getToken()) || !user.getResetToken().equals(request.getToken())) {
            throw new InvalidJwtAuthenticationException("Redefinition token has expired, please, click forgot password again.");
        }
        user.setResetToken(null);
        user.setPassword(this.encoderPassword().encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public TokenDTO signIn(AccountCredentialsDTO loginData) {
        if (checkIfParamsIsNotNull(loginData)) {
            throw new NullEntityFieldException("Invalid Client Request: Email or Username is null or blank.");
        }
        String email = loginData.getEmail();
        String password = loginData.getPassword();
        TokenDTO tokenResponse = new TokenDTO();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            throw new CustomBadCredentialsException("Username or password is invalid");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Email: " + email + " not found.");
        }
        if (!user.isVerified()) {
            throw new UnverifiedEmailException("Email not verified. Please check your inbox.");
        }
        return tokenProvider.createAccessToken(email, user.getRoles());

    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void validateEmail(User user) {
        user.setVerificationToken(null);
        user.setVerified(true);
        userRepository.save(user);
    }

    public boolean checkIfParamsIsNotNull(AccountCredentialsDTO dataLogin) {
        return dataLogin == null || dataLogin.getEmail() == null || dataLogin.getEmail().isBlank() || dataLogin.getPassword() == null || dataLogin.getPassword().isBlank();
    }

    private PasswordEncoder encoderPassword() {
        return new BCryptPasswordEncoder();
    }

    private <T> List<String> validFields(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();
    }
}
