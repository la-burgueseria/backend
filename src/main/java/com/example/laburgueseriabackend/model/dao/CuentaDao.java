package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.Cuenta;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CuentaDao extends JpaRepository<Cuenta, Integer> {
    //obtener cuentas por fecha
    @Query("SELECT c FROM Cuenta  c WHERE c.fecha >= :fechaInicio AND c.fecha <= :fechaFin ORDER BY c.fecha ASC")
    List<Cuenta> getcuentasByFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
