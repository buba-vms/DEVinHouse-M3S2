package com.spring.security.clamed.controllers;

import com.spring.security.clamed.dto.UsuarioInput;
import com.spring.security.clamed.dto.UsuarioOutput;
import com.spring.security.clamed.model.Usuario;
import com.spring.security.clamed.repository.UsuarioRepository;
import com.spring.security.clamed.service.UsuarioService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController  {


    @Autowired
    private UsuarioService usuarioService;


    @PostMapping
    public ResponseEntity<UsuarioOutput> cadastrar(@RequestBody UsuarioInput usuarioInput){
        Usuario usuario = toModel(usuarioInput);
        UsuarioOutput usuarioOutput = toObjectOutPut(usuarioService.salvar(usuario));
        return new ResponseEntity<UsuarioOutput>(usuarioOutput, HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<UsuarioOutput> atualizar(@RequestBody UsuarioInput usuarioInput){
        Usuario usuario = toModel(usuarioInput);
        UsuarioOutput usuarioOutput = toObjectOutPut(usuarioService.salvar(usuario));
        return new ResponseEntity<UsuarioOutput>(usuarioOutput, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam Long idUsuario){

        usuarioService.delete(idUsuario);
        return new ResponseEntity<String>("Usuário deletado com sucesso.",HttpStatus.OK);
    }

    @DeleteMapping(value = "/{idUsuario}")
    public ResponseEntity<String> deletePathVariable(@PathVariable(value = "idUsuario") Long idUsuario){

        usuarioService.delete(idUsuario);
        return new ResponseEntity<String>("Usuário deletado com sucesso.",HttpStatus.OK);
    }


    @GetMapping(value = "/")
    public ResponseEntity<List<UsuarioOutput>> getUsersByName(@RequestParam (name = "nome") String nome){

        // obtem a lista de usuários model
        List<Usuario> usuarios = usuarioService.findUsersByName(nome);
        // converte lista de Usuario para lista de UsuarioOutput
        List<UsuarioOutput> usuariosOutput = toCollectionDTOOutput(usuarios);
        return new ResponseEntity<List<UsuarioOutput>>(usuariosOutput, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioOutput>> getUsuarios(){
       // obtem a lista de usuarios cadastrados
        List<Usuario> usuarios = usuarioService.getUsers();
       // converte lista de Usuario para lista de UsuarioOutput
       List<UsuarioOutput> usuariosOutput = toCollectionDTOOutput(usuarios);

        return new ResponseEntity<List<UsuarioOutput>>(usuariosOutput, HttpStatus.OK);
    }

    // método para fazer a conversão de DTO de entrada (UsuarioInput) para Model (Usuario)
    private Usuario toModel(UsuarioInput usuarioInput){
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioInput, usuario);
        return usuario;

    }

    // método para fazer a conversão de Model para DTO de saída (UsuarioOutput)
    private UsuarioOutput toObjectOutPut(Usuario usuario){
        UsuarioOutput usuarioOutput = new UsuarioOutput();
        BeanUtils.copyProperties(usuario, usuarioOutput);
        return usuarioOutput;
    }

    // método para converter uma lista de Usuarios (List<Usuario>) para uma lista de DTO de saída (List<UsuarioOutput>)
    private List<UsuarioOutput> toCollectionDTOOutput(List<Usuario> usuarios){
        return usuarios.stream()
                .map(usuario -> toObjectOutPut(usuario))
                .collect(Collectors.toList());
    }


}
