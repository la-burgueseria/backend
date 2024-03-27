package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.controller.auth.AuthResponse;
import com.example.laburgueseriabackend.jwt.JwtService;
import com.example.laburgueseriabackend.model.dao.EmpleadoDao;
import com.example.laburgueseriabackend.model.dao.UsuariosDao;
import com.example.laburgueseriabackend.model.dto.UsuariosDto;
import com.example.laburgueseriabackend.model.entity.Empleado;
import com.example.laburgueseriabackend.model.entity.usuarios.Roles;
import com.example.laburgueseriabackend.model.entity.usuarios.Usuarios;
import com.example.laburgueseriabackend.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthImplService implements IAuthService {
    private final UsuariosDao usuariosDao;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(UsuariosDto request) {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails user = usuariosDao.findByUsername(request.getUsername()).orElseThrow();
            Usuarios usuario = usuariosDao.findByUsername(request.getUsername()).orElseThrow();
            String token = jwtService.getToken(user);

            // Verificar si el token está caducado
            if (jwtService.isTokenExpired(token)) {
                // Generar un nuevo token
                token = jwtService.refreshToken(token);
            }

            return AuthResponse.builder()
                    .token(token)
                    .nombre(usuario.getNombre())
                    .apellido(usuario.getApellido())
                    .rol(String.valueOf(usuario.getRol()))
                    .build();

    }

    @Transactional
    @Override
    public AuthResponse register(UsuariosDto request) {
        Usuarios usuario = Usuarios.builder()
                .username(request.getUsername())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .correo(request.getCorreo())
                .estado(true)
                .build();

        usuariosDao.save(usuario);

        return AuthResponse.builder()
                .token(jwtService.getToken(usuario))
                .build();
    }

    @Override
    public Usuarios getUsuarioByDocumento(String documento) {
        return usuariosDao.findUsuariosByUsername(documento);
    }

    @Override
    public List<Usuarios> listAll() {
        return usuariosDao.findAll();
    }

    @Transactional
    @Override
    public void updateUsuario(UsuariosDto usuarioDto) {
        Usuarios usuario = Usuarios.builder()
                .id(usuarioDto.getId())
                .nombre(usuarioDto.getNombre())
                .apellido(usuarioDto.getApellido())
                .username(usuarioDto.getUsername())
                .password(usuarioDto.getPassword())
                .rol(usuarioDto.getRol())
                .correo(usuarioDto.getCorreo())
                .build();

        usuariosDao.updateUsuario(usuario.getUsername(), usuario.getId() ,usuario.getNombre(), usuario.getApellido(), usuario.getPassword(), usuario.getRol(), usuario.getCorreo());
    }

    @Override
    public Usuarios findUsuariosByCorreo(String correo) {
        Usuarios usuarios = usuariosDao.findUsuariosByCorreo(correo);
        return usuarios;
    }

    @Override
    public void updateToken(String token, Integer id) {
        usuariosDao.updateToken(token, id);
    }
}
