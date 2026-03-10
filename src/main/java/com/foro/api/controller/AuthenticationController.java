package com.foro.api.controller;

import com.foro.api.domain.usuario.DatosAutenticacionUsuario;
import com.foro.api.domain.usuario.Usuario;
import com.foro.api.infra.security.DatosTokenJWT;
import com.foro.api.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<DatosTokenJWT> autenticarUsuario(
            @RequestBody @Valid DatosAutenticacionUsuario datos) {

        var authToken = new UsernamePasswordAuthenticationToken(
                datos.login(), datos.clave());

        var usuarioAutenticado = authenticationManager.authenticate(authToken);

        var tokenJWT = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());

        return ResponseEntity.ok(new DatosTokenJWT(tokenJWT));
    }
}
