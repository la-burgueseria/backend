package com.example.laburgueseriabackend.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EgresoDto {
    private Integer id;
    private LocalDateTime fecha;
    private String descripcion;
    private Double total;
    private String categoria;
    private String deduccionDesde;
}
