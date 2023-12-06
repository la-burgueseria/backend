package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.Cuenta;
import com.example.laburgueseriabackend.model.entity.Ingreso;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngresoCuentaDto {
    private Integer id;
    private Ingreso ingreso;
    private Cuenta cuenta;
}
