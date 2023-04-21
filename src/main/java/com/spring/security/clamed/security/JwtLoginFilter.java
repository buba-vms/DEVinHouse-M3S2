package com.spring.security.clamed.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.clamed.model.Usuario;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* Estabelece nosso gerenciador de token */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    protected  JwtLoginFilter(String url, AuthenticationManager authenticationManager){

        // Obrigamos a realizar a autenticação na URL
        super(new AntPathRequestMatcher(url));

        //Setamos o gerenciador de autenticação
        setAuthenticationManager(authenticationManager);

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        //Pegar o token do usuário para validar
        Usuario usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

        // retornamos o login, senha e acessos para o autenticador
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                usuario.getLogin(),
                usuario.getSenha()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        new JwtTokenAutenticacaoService().addAuthentication(response, authResult.getName());
    }
}
