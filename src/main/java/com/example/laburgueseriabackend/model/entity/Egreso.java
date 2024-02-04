package com.example.laburgueseriabackend.model.entity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    private LocalDateTime fecha;
    @Column(name = "total")
    private Double total;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "categoria")
    private String categoria;

    @Column(name = "deduccion_desde", nullable = true)
    private String deduccionDesde;
    //soporte de los egresos
    @Column(name = "soporte", length = 1000000, nullable = true)
    private byte[] soporte;
    //asignar la hora actual UTC al momento en el que se crea un nuevo registro en la entidad
    @PrePersist
    protected void onCreate(){fecha = LocalDateTime.now(ZoneOffset.UTC);}
}
