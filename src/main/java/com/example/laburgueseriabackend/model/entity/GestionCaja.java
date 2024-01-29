package com.example.laburgueseriabackend.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "gestion_caja")
public class GestionCaja implements Serializable {
    /*
    * Al iniciar el día se crea una instancia de CierreCaja con solo los valores
    * Id y fechaHoraInicio, esto nos va a permitir saber al día siguiente si se cerró la caja
    *
    * */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "total_calculado", nullable = true)
    private Double totalCalculado;
    @Column(name = "total_reportado", nullable = true)
    private Double totalReportado;
    @Column(name = "saldo_inicio_caja_menor", nullable = true)
    private Double saldoInicioCajaMenor;
    @Column(name = "observaciones", nullable = true)
    private String observaciones;
    @Column(name = "fecha_hora_inicio", nullable = true)
    private LocalDateTime fechaHorainicio;
    @Column(name = "fecha_hora_cierre", nullable = true)
    private LocalDateTime fechaHoraCierre;
    //campo para saber si la caja esta bierta o no
    @Column(name = "estado_caja")
    private Boolean estadoCaja;
}
