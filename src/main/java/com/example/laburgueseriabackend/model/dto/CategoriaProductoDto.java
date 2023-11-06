package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.Producto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
public class CategoriaProductoDto {
    private Integer id;
    private String nombre;
    @JsonIgnore
    private List<Producto> productos;
}
