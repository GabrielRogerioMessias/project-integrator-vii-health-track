package org.projetointegrador.unifio.projectintegratorviibackend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.projetointegrador.unifio.projectintegratorviibackend.controllers.exceptions.StandardError;
import org.projetointegrador.unifio.projectintegratorviibackend.security.exceptions.InvalidJwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.time.Instant;

public class JwtTokenFilter extends GenericFilterBean {


    private final JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.tokenProvider = jwtTokenProvider;
    }

    @Override
    // filtro executado em cada requisição
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            //O método ResolveToken, vai obter do header da request o token e vai retornar o token sem o Bearer
            String token = tokenProvider.resolveToken((HttpServletRequest) request);
            if (token != null && tokenProvider.validateToken(token)) {// valida o token
                Authentication authentication = tokenProvider.getAuthentication(token);// após validar, ele obtem uma validação
                if (authentication != null) {//se ele conseguir validar, ele seta a validação na seção do spring
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
            // deixa a requisição seguir adiante
            filterChain.doFilter(request, response);
        } catch (InvalidJwtAuthenticationException ex) {
            //caso o token esteja valido
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

            StandardError error = new StandardError(
                    Instant.now(),
                    HttpStatus.FORBIDDEN.value(),
                    "Token expirado, faça login novamente.",
                    ex.getMessage(),
                    ((HttpServletRequest) request).getRequestURI()
            );
            // necessário, pois o mapper não sabe serializar o java.time.Instant, então registramos o jackson-datatype-jsr310 no filtro
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            httpResponse.getWriter().write(mapper.writeValueAsString(error));
        }
    }


//    desabilitado
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        //O método ResolveToken, vai obter do header da request o token e vai retornar o token sem o Bearer
//        String token = tokenProvider.resolveToken((HttpServletRequest) request);//obter token através da request
//        try {
//            if (token != null && tokenProvider.validateToken(token)) {// valida o token
//                Authentication authentication = tokenProvider.getAuthentication(token);// após validar, ele obtem uma validação
//                if (authentication != null) {//se ele conseguir validar, ele seta a validação na seção do spring
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }
//        } catch (InvalidJwtAuthenticationException e) {
//            throw e;
//        }
//        chain.doFilter(request, response);
//    }

}