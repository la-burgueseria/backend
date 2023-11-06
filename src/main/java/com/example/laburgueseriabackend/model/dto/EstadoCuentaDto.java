package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.Cuenta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoCuentaDto {
    private Integer id;
    private String nombre;
    @JsonIgnore
    private List<Cuenta> cuentas;
}
