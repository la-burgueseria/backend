package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.CategoriaProducto;
import com.example.laburgueseriabackend.model.entity.EstadoMesa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EstadoMesaDao extends CrudRepository<EstadoMesa, Integer> {

    @Query("SELECT em from EstadoMesa em WHERE em.nombre = :nombre")
    EstadoMesa findByNombre(String nombre);
}
