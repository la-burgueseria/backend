package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.EstadoMesaDto;
import com.example.laburgueseriabackend.model.dto.QrDto;
import com.example.laburgueseriabackend.model.entity.EstadoMesa;
import com.example.laburgueseriabackend.model.entity.Qr;

import java.util.List;

public interface IQrService {
    Qr save(QrDto qrDto);
    //buscar categoria por id
    Qr findById(Integer id);
    //eliminar categoria
    void delete(Qr qr);
    //obtener todos las categorias
    List<Qr> listAll();
    Qr findByRuta(String url);
    Boolean existsById(Integer id);
}
