package com.spring.security.clamed.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role implements GrantedAuthority {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeRole; /* Papel ROLE_ADMINISTRADOR, ROLE_CADASTRADOR */

    @Override
    public String getAuthority() {
        return this.nomeRole;
    }
}
