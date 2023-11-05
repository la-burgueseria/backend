package com.example.laburgueseriabackend.model.dto;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class MesaDto {
    private Integer id;
    private Integer numeroMesa;
    private QrDto qr;
    private EstadoMesaDto estadoMesa;

}

