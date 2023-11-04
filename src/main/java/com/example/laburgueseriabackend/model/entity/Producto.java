package com.example.laburgueseriabackend.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name="producto")
public class Producto implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "precio")
    private Double precio;
    @Column(name = "imagen")
    private String imagen;
    @Column(name = "descripcion")
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "categoria_producto_id")
    private CategoriaProducto categoriaProducto;
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InsumosPorProducto> insumosPorProductos;


    //IMPORTANTE CREAR EL GETTER DEL LA CONEXION para indicar las referencias
    //TANTO EN EL ONETOMANY COMO EN EL MANYTOONE
    @JsonBackReference
    public CategoriaProducto getCategoriaProducto(){
        return categoriaProducto;
    }

    //referencia a insumos pro producto
    @JsonManagedReference
    public  List<InsumosPorProducto> getInsumosPorProductos(){
        return insumosPorProductos;
    }
}
