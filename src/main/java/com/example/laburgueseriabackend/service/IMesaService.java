package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.MesaDto;
import com.example.laburgueseriabackend.model.dto.ProductoDto;
import com.example.laburgueseriabackend.model.entity.Mesa;
import com.example.laburgueseriabackend.model.entity.Producto;

import java.util.List;

public interface IMesaService {
    Mesa save(MesaDto mesaDto);
    Mesa findById(Integer id);
    void delete(Mesa mesa);
    Mesa findByNumMesa(Integer numeroMesa);
    Mesa findByQrId(Integer qrId);
    List<Mesa> listAll();

    Boolean existsById(Integer id);
}
