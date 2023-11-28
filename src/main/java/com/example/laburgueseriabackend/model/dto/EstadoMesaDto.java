package com.example.laburgueseriabackend.model.dto;

import com.example.laburgueseriabackend.model.entity.Mesa;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private List<Mesa> mesas;
}
