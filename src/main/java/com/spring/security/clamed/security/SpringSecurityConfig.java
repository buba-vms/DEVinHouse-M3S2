package com.spring.security.clamed.security;

import com.spring.security.clamed.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioService usuarioService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                // Quais requisições serão autorizadas e como sera a autorização
                .authorizeRequests()

                /*Quais URLs vamos filtrar? É para permitir ou para bloquear?
                O método antMatchers tem 3 sobrecargas
                1 - Passando somente a URL
                2 - Passando só o método GET, POST, PUT, DELETE
                3 - Passando o método HTTP e a URL
                */
                .antMatchers(HttpMethod.GET, "/usuarios")
                //Permite todos os acessos
                .permitAll()


                // permite que usuarios com a role CADASTRADOR possam inserir, atualizar e obter
                .antMatchers(HttpMethod.POST,"/usuarios/**").hasRole("CADASTRADOR")
                .antMatchers(HttpMethod.PUT,"/usuarios/**").hasRole("CADASTRADOR")
                .antMatchers(HttpMethod.GET,"/usuarios/**").hasRole("CADASTRADOR")


                // permite o acesso a todos os recursos de /usuarios/** para o perfil ADMISTRADOR
                .antMatchers("/usuarios/**").hasRole("ADMINISTRADOR")

                //.antMatchers(HttpMethod.GET,"/usuarios/").hasRole("ADMINISTRADOR")

                //.antMatchers("/usuarios/**").hasRole("CADASTRADOR")

                // De qualquer requisição
                .anyRequest()

                // Aceitar somente requisições autenticadas
                .authenticated()

                // e
                .and()

                /* CSRF é um tipo de ataque a aplicações web. Porém será feita por token a proteção a esse ataque
                 é dispensável. */
                .csrf().disable()

                // Definimos que não iremos criar sessão para armazenar os dados do usuário
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                /*Filtrar as requisições de login para fazer autenticação*/
                .addFilterBefore(new JwtLoginFilter("/login", authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)

                 /* Filtrar as demais requisições para verificar a preservação do token JWT no header do HTTP */
                .addFilterBefore(new JwtApiAutenticaoFilter(), UsernamePasswordAuthenticationFilter.class);


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Service que irá consultar o usuário no banco de dados
        auth.userDetailsService(usuarioService)

        // definir a codificação de senha
                .passwordEncoder(new BCryptPasswordEncoder());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // configura URLs para não passar pelos filtros de segurança
        web.ignoring().antMatchers("/**.html",
                "/v2/api-docs",
                "/webjars/**");
    }
}
