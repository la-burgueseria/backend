package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.EmpleadoCuenta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoDto {
    private Integer id;
    private String documento;
    private String nombre;
    private String apellido;
    private Boolean estado;
    @JsonIgnore
    private List<EmpleadoCuenta> empleadoCuentas;
}
