package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.Producto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProductoDao extends CrudRepository<Producto, Integer> {
    @Query("SELECT p from Producto p WHERE p.nombre = :nombre")
    Producto findByNombre(String nombre);
}
