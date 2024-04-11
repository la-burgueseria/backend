package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.Cuenta;
import com.example.laburgueseriabackend.model.entity.EmpleadoCuenta;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CuentaDao extends JpaRepository<Cuenta, Integer> {
    //obtener cuentas por fecha
    @Query("SELECT c FROM Cuenta  c WHERE c.fecha >= :fechaInicio AND c.fecha <= :fechaFin ORDER BY c.fecha DESC")
    List<Cuenta> getcuentasByFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    //obtener las cuentas de un empleado dado filtradas por fecha
    @Query("SELECT ec.cuenta FROM EmpleadoCuenta ec WHERE ec.empleado.id = :empleadoId AND ec.cuenta.fecha >= :fechaInicio AND ec.cuenta.fecha <= :fechaFin ORDER BY ec.cuenta.fecha ASC")
    List<Cuenta> getCuentasByEmpleado(Integer empleadoId, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    //buscar cuentas por numero de mesa y que la mesa este ocupada
    @Query("SELECT c from Cuenta  c WHERE c.mesa.numeroMesa = :numeroMesa AND c.mesa.isOcupada = true AND (c.estadoCuenta.id != 2 AND c.estadoCuenta.id != 4)")
    Cuenta getCuentasActivasByNumeroMesa(Integer numeroMesa);
}
