package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.CategoriaProductoDto;
import com.example.laburgueseriabackend.model.dto.EstadoMesaDto;
import com.example.laburgueseriabackend.model.entity.CategoriaProducto;
import com.example.laburgueseriabackend.model.entity.EstadoMesa;

import java.util.List;

public interface IEstadoMesaService {
    //guardar categoria
    EstadoMesa save(EstadoMesaDto estadoMesaDto);
    //buscar categoria por id
    EstadoMesa findById(Integer id);
    //eliminar categoria
    void delete(EstadoMesa estadoMesa);
    //obtener todos las categorias
    List<EstadoMesa> listAll();
    EstadoMesa findByNombre(String nombre);
    Boolean existsById(Integer id);
}
