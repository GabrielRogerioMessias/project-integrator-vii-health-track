package org.projetointegrador.unifio.projectintegratorviibackend.services;

import org.projetointegrador.unifio.projectintegratorviibackend.models.User;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.securityDTO.AccountCredentialsDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.models.dtos.securityDTO.TokenDTO;
import org.projetointegrador.unifio.projectintegratorviibackend.repositories.UserRepository;
import org.projetointegrador.unifio.projectintegratorviibackend.security.exceptions.CustomBadCredentialsException;
import org.projetointegrador.unifio.projectintegratorviibackend.security.jwt.JwtTokenProvider;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.NullEntityFieldException;
import org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions.UnverifiedEmailException;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
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
}
