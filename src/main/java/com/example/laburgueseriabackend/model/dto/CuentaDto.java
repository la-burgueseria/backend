package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    private LocalDateTime fecha;
    @JsonIgnore
    private List<EmpleadoCuenta> empleadoCuentas;

}
