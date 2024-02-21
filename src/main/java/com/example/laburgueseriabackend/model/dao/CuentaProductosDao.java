package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.CuentaProductos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CuentaProductosDao extends JpaRepository<CuentaProductos, Integer> {
    //obtener todos los produtos vinculados a una cuenta X
    @Query("SELECT cp FROM CuentaProductos cp WHERE cp.cuenta.id = :cuentaId")
    List<CuentaProductos> getCuentaProductosByCuenta(Integer cuentaId);

    @Modifying
    @Query("DELETE FROM CuentaProductos cp WHERE cp.id = :id")
    void deleteCuentaProducto(Integer id);
}
