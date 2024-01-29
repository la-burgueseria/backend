package com.example.laburgueseriabackend.model.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GestionCajaDto {
    private Integer id;
    private Double totalCalculado;
    private Double totalReportado;
    private Double saldoInicioCajaMenor;
    private String observaciones;
    private LocalDateTime fechaHorainicio;
    private LocalDateTime fechaHoraCierre;
    private Boolean estadoCaja;
}
