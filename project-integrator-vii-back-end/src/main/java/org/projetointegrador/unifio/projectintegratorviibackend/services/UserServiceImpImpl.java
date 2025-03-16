package org.projetointegrador.unifio.projectintegratorviibackend.services;

import org.projetointegrador.unifio.projectintegratorviibackend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserServiceImpImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Email: " + email + " not found.");
        }
    }
}
