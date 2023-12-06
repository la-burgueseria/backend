package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.IngresoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngresoCuentaDao extends JpaRepository<IngresoCuenta, Integer> {
    //SELECIONAR TODAS LAS CUENTAS DE UN INGRESO EN ESPECIFICO
    @Query("SELECT ic FROM IngresoCuenta ic WHERE ic.ingreso.id = :ingresoId")
    List<IngresoCuenta> obtenerCuentasPorIngreso(Integer ingresoId);
}
