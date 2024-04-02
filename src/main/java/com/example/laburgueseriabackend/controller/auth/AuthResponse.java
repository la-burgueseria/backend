package com.example.laburgueseriabackend.controller.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthResponse {
    String token;
    String nombre;
    String apellido;
    String rol;
    Integer empleadoId;
}
