package com.example.laburgueseriabackend.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@Builder
public class QrDto implements Serializable {
    private Integer id;
    private String ruta;
    private String url;
}
