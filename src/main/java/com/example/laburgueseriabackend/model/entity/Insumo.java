package com.example.laburgueseriabackend.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data //anotacion de lombok crea automaticament todos los metodos basicos, setters y getters
@AllArgsConstructor // anotacion de lombok, genera un constructor con todos los argumentos de la clase
@NoArgsConstructor //anotacion de lombok, genera un constructor vacio, sin argumentos
@ToString //anotacion de lombok, genera el metodo ToString
@Builder
@Entity // anotacion que especifica que este modelo hace referencia a una entidad de la base de datos
@Table(name = "insumo") // se especifica a que tabla hace referencia
public class Insumo implements Serializable {
    //declaracion de los campos de la base de datos
    //para la tabla insumo
    @Id //anotacion que especifica que este atributo es el ID de la tabla
    @Column(name = "id") // referencia el nombre de la columna asignada a esta variable
    @GeneratedValue(strategy = GenerationType.IDENTITY) //se especifica que el ID es autoincrement
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "cantidad")
    private Integer cantidad;


}
