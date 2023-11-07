package com.example.laburgueseriabackend.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "insumos_por_producto")
public class InsumosPorProducto implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //se especifica que el ID es autoincrement
    private Integer id;

    @Column(name = "cantidad")
    private Integer cantidad;
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "insumo_id")
    private Insumo insumo;
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;


}
