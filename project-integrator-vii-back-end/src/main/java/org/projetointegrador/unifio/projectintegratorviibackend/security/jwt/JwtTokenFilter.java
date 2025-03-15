package org.projetointegrador.unifio.projectintegratorviibackend.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.projetointegrador.unifio.projectintegratorviibackend.security.exceptions.InvalidJwtAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {


    private final JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.tokenProvider = jwtTokenProvider;
    }

    //esse filtro sera executado a cada requisição
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //O método ResolveToken, vai obter do header da request o token e vai retornar o token sem o Bearer
        String token = tokenProvider.resolveToken((HttpServletRequest) request);//obter token através da request
        try {
            if (token != null && tokenProvider.validateToken(token)) {// valida o token
                Authentication authentication = tokenProvider.getAuthentication(token);// após validar, ele obter uma validação
                if (authentication != null) {//se ele conseguir validar, ele seta a validação na seção do spring
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (InvalidJwtAuthenticationException e) {
            throw new RuntimeException(e);
        }
        chain.doFilter(request, response);
    }

}