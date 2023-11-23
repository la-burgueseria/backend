package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductoDao extends JpaRepository<Producto, Integer> {
    @Query("SELECT p from Producto p WHERE p.nombre = :nombre")
    Producto findByNombre(String nombre);
    //buscar insumos que coincidan con el parámetro de busqueda
    @Query("SELECT p FROM Producto  p WHERE p.nombre LIKE %:nombre%")
    List<Producto> findProductoByNombre(String nombre);
}
