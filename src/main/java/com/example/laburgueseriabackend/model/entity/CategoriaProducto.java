package com.example.laburgueseriabackend.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "categoria_producto")
public class CategoriaProducto implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "categoriaProducto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Producto> productos;

    //IMPORTANTE CREAR EL GETTER DEL LA CONEXION para indicar las referencias
    //TANTO EN EL ONETOMANY COMO EN EL MANYTOONE
    @JsonManagedReference
    public List<Producto> getProductos(){
        return productos;
    }
}
