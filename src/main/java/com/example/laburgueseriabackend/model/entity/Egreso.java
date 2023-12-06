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
@Table(name = "egreso")
public class Egreso implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "fecha")
    private String fecha;
    @Column(name = "total")
    private Double total;
    @Column(name = "Observaciones")
    private String observaciones;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_egreso_id")
    private TipoEgreso tipoEgreso;
    @OneToMany(mappedBy = "egreso", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    @JsonIgnore
    private List<ReporteEgreso> reporteEgresos;
}
