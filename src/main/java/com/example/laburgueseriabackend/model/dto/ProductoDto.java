package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.CategoriaProducto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto implements Serializable {
    private Integer id;
    private String nombre;
    private Double precio;
    private byte[] imagen;
    private String descripcion;
    private Boolean isPublicado;
    private CategoriaProductoDto categoriaProductoDto;
    @JsonIgnore
    private List<InsumosPorProductoDto> insumosPorProductosDto;
}
