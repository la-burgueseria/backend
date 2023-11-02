package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.CategoriaProducto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CategoriaProductoDao extends CrudRepository<CategoriaProducto, Integer> {

    //verificar si una categoria con el mismo nombre ya existe
    @Query("SELECT c from CategoriaProducto c WHERE c.nombre = :nombre")
    CategoriaProducto findByNombre(String nombre);
}
