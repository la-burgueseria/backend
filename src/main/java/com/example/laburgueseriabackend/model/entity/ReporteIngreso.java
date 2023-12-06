package com.example.laburgueseriabackend.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "reporte_ingreso")
public class ReporteIngreso implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "ingreso_id")
    private Ingreso ingreso;
    @ManyToOne
    @JoinColumn(name = "reporte_mensual_id")
    private ReporteMensual reporteMensual;
}
