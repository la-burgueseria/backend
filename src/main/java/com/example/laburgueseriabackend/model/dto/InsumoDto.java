package com.example.laburgueseriabackend.model.dto;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data //anotacion de lombok crea automaticament todos los metodos basicos, setters y getters
@ToString //anotacion de lombok, genera el metodo ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsumoDto implements Serializable {

    private Integer id;
    private String nombre;
    private Integer cantidad;
    private Integer precioCompraUnidad;
}
