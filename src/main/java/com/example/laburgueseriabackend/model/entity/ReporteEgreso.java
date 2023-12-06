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
@Table(name = "reporte_egreso")
public class ReporteEgreso implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "egreso_id")
    private Egreso egreso;
    @ManyToOne
    @JoinColumn(name = "reporte_mensual_id")
    private ReporteMensual reporteMensual;
}
