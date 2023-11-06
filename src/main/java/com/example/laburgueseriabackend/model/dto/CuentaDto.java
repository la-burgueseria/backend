package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.CuentaProductos;
import com.example.laburgueseriabackend.model.entity.EstadoCuenta;
import com.example.laburgueseriabackend.model.entity.Mesa;
import com.example.laburgueseriabackend.model.entity.Producto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaDto {
    private Integer id;
    private Mesa mesa;
    private EstadoCuenta estadoCuenta;
    @JsonIgnore
    private List<CuentaProductos> cuentaProductos;
    @JsonIgnore
    private String fecha;

}
