package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.usuarios.Roles;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuariosDto {
    private Integer id;
    private String nombre;
    private String apellido;
    private String username;
    private String password;
    private String rol;
    private Boolean estado;
    private String correo;
    private String token;
}
