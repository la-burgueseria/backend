package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.ProductoDto;
import com.example.laburgueseriabackend.model.entity.Producto;

import java.util.List;

public interface IProductoService {
    Producto save(ProductoDto productoDto);
    Producto findById(Integer id);
    void delete(Producto producto);
    Producto findByNombre(String nombre);

    List<Producto> listAll();

    Boolean existsById(Integer id);
}
