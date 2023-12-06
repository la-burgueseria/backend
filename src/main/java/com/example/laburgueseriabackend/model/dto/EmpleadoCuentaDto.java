package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.Cuenta;
import com.example.laburgueseriabackend.model.entity.Empleado;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoCuentaDto {
    private Integer id;
    private Empleado empleado;
    private Cuenta cuenta;
}
