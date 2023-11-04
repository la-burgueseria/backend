package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.CategoriaProducto;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@Builder
public class ProductoDto implements Serializable {
    private Integer id;
    private String nombre;
    private Double precio;
    private String imagen;
    private String descripcion;
    private CategoriaProductoDto categoriaProductoDto;
    private List<InsumosPorProductoDto> insumosPorProductosDto;
}
