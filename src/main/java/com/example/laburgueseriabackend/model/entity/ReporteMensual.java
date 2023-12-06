package com.example.laburgueseriabackend.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "reporte_mensual")
public class ReporteMensual implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "fecha_inicio")
    private String fechaInicio;
    @Column(name = "fecha_fin")
    private String fechaFin;
    //relacion con ingreso y egreso
    @OneToMany(mappedBy = "reporteMensual", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    @JsonIgnore
    private List<ReporteIngreso> reporteIngresos;
    @OneToMany(mappedBy = "reporteMensual", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    @JsonIgnore
    private List<ReporteEgreso> reporteEgresos;


}
