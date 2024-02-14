package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.usuarios.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuariosDao extends JpaRepository<Usuarios, Integer> {

    Optional<Usuarios> findByUsername(String username);
    @Query("SELECT u FROM Usuarios  u WHERE u.username = :username")
    Usuarios findUsuariosByUsername(String username);

    @Modifying()
    @Query("UPDATE Usuarios u SET u.username = :username, u.nombre = :nombre, u.apellido = :apellido, u.password = :password, u.rol = :rol WHERE u.id = :id")
    void updateUsuario(String username, Integer id, String nombre, String apellido, String password, String rol);
}
