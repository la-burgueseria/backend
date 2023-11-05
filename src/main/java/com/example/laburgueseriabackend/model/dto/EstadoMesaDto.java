package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.Mesa;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Builder
public class EstadoMesaDto {
    private Integer id;
    private String nombre;
    private List<Mesa> mesas;
}
