package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface IngresoDao extends JpaRepository<Ingreso, Integer> {
    @Query("SELECT i from Ingreso i WHERE i.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Ingreso> findIngresoByFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
