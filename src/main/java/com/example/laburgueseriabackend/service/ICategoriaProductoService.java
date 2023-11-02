package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.CategoriaProductoDto;
import com.example.laburgueseriabackend.model.entity.CategoriaProducto;

import java.util.List;

//TODO
public interface ICategoriaProductoService {

    //guardar categoria
    CategoriaProducto save(CategoriaProductoDto categoriaProductoDto);
    //buscar categoria por id
    CategoriaProducto findById(Integer id);
    //eliminar categoria
    void delete(CategoriaProducto categoriaProducto);
    //obtener todos las categorias
    List<CategoriaProducto> listAll();
    CategoriaProducto findByNombre(String nombre);
    Boolean existsById(Integer id);

}
