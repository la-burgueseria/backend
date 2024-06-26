package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.Cuenta;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngresoDto {
    private Integer id;
    private LocalDateTime fecha;
    private String metodoPago;
    private Double total;
    private Cuenta cuenta;
}
