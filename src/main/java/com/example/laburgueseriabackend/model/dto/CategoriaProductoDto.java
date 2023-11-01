package com.example.laburgueseriabackend.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class CategoriaProductoDto {
    private Integer id;
    private String nombre;
}
