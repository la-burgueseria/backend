package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.EstadoCuenta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EstadoCuentaDao extends CrudRepository<EstadoCuenta, Integer> {
    @Query("SELECT ec FROM EstadoCuenta ec WHERE ec.nombre = :nombre")
    EstadoCuenta findByNombre(String nombre);
}
