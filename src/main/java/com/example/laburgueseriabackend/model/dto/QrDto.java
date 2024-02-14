package com.example.laburgueseriabackend.model.dto;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrDto implements Serializable {
    private Integer id;
    private String ruta;
    private String url;
}
