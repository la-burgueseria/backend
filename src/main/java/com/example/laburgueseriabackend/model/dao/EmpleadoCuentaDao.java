package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.EmpleadoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmpleadoCuentaDao extends JpaRepository<EmpleadoCuenta, Integer> {
    //buscar cuentas de un empleado especifico
    @Query("SELECT ec FROM EmpleadoCuenta ec WHERE ec.empleado.id = :id")
    List<EmpleadoCuenta> cuentasDeEmpleado(Integer id);
    //buscar empleado asociado a una cuenta
    @Query("SELECT ec FROM EmpleadoCuenta ec WHERE ec.cuenta.id = :id")
    EmpleadoCuenta empleadoEnCuenta(Integer id);
}
