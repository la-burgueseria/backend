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
@Table(name = "ingreso_cuenta")
public class IngresoCuenta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "ingreso_id")
    private Ingreso ingreso;
    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;
}
