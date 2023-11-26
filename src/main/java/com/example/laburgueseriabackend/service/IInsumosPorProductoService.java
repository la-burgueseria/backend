package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.InsumosPorProductoDto;
import com.example.laburgueseriabackend.model.entity.InsumosPorProducto;

import java.util.List;

public interface IInsumosPorProductoService {

    InsumosPorProducto save(InsumosPorProductoDto insumosPorProductoDto);
    InsumosPorProducto findById(Integer id);
    void delete(InsumosPorProducto insumosPorProducto);
    List<InsumosPorProducto> listAll();
    Boolean existsById(Integer id);
    List<InsumosPorProducto> insumoPorProductoExists(Integer idInsumo, Integer idProducto);

    List<InsumosPorProducto> selecionarInsumosDelProducto(Integer idProducto);
}
