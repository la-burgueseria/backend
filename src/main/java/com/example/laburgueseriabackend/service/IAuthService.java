package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.controller.auth.AuthResponse;
import com.example.laburgueseriabackend.model.dto.UsuariosDto;
import com.example.laburgueseriabackend.model.entity.usuarios.Usuarios;

import java.util.List;

public interface IAuthService {
    AuthResponse login(UsuariosDto request);
    AuthResponse register(UsuariosDto request);

    Usuarios getUsuarioByDocumento(String documento);
    List<Usuarios> listAll();
    void updateUsuario(UsuariosDto usuario);
}
