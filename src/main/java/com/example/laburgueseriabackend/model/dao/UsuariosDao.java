package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.usuarios.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UsuariosDao extends JpaRepository<Usuarios, Integer> {

    Optional<Usuarios> findByUsername(String username);
    @Query("SELECT u FROM Usuarios  u WHERE u.username = :username")
    Usuarios findUsuariosByUsername(String username);
    //filtrar por correo electronico
    @Query("select u FROM Usuarios  u WHERE u.correo = :correo")
    Usuarios findUsuariosByCorreo(String correo);
    //asignar token a un usuario existente a traves del correo electronico
    @Transactional
    @Modifying()
    @Query("UPDATE Usuarios u SET u.token = :token WHERE u.id = :id")
    void updateToken(String token, Integer id);
    @Modifying()
    @Query("UPDATE Usuarios u SET u.username = :username, u.nombre = :nombre, u.apellido = :apellido, u.password = :password, u.rol = :rol, u.correo = :correo WHERE u.id = :id")
    void updateUsuario(String username, Integer id, String nombre, String apellido, String password, String rol, String correo);
}
