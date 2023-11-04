package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.Insumo;
import com.example.laburgueseriabackend.model.entity.Producto;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class InsumosPorProductoDto {
    private Integer id;
    private Integer cantidad;
    private Insumo insumoDto;
    private Producto productoDto;
}
