package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.Egreso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EgresoDao extends JpaRepository<Egreso, Integer> {
    //paginacion filtrada por fechas
    Page<Egreso> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    //obtener egresos entre fechas
    @Query("SELECT e FROM Egreso e WHERE e.fecha >= :fechaInicio AND e.fecha <= :fechaFin ORDER BY e.fecha ASC")
    List<Egreso> findEgresoByFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
