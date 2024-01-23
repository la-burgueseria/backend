package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.Ingreso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface IngresoDao extends JpaRepository<Ingreso, Integer> {
    @Query("SELECT i FROM Ingreso  i WHERE i.fecha >= :fechaInicio AND i.fecha <= :fechaFin ORDER BY i.fecha ASC")
    List<Ingreso> findIngresoByFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    //paginacion filtrada por fechas
    Page<Ingreso> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
}
