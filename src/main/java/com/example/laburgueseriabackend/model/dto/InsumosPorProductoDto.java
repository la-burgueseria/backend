package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.Insumo;
import com.example.laburgueseriabackend.model.entity.Producto;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsumosPorProductoDto {
    private Integer id;
    private Integer cantidad;
    private Insumo insumoDto;
    private Producto productoDto;
}
