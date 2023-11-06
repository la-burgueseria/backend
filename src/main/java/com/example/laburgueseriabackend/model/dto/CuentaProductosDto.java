package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.Cuenta;
import com.example.laburgueseriabackend.model.entity.Producto;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaProductosDto {
    private Integer id;
    private Cuenta cuenta;
    private Producto producto;
    private Integer cantidad;

}
