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
@Table(name = "cuenta_productos")
public class CuentaProductos implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "total")
    private Double total;
}
