package com.spring.security.clamed.repository;

import com.spring.security.clamed.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("select u from Usuario u where u.nome like %?1%")
    List<Usuario> findUsersByName(String nome);


    // query necessária para a configuração de auteticação com JWT para o Spring Security
    @Query("select u from Usuario u where u.login = ?1")
    Usuario findUserByLogin(String login);

}
