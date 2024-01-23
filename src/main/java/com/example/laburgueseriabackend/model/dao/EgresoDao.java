package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.Egreso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface EgresoDao extends JpaRepository<Egreso, Integer> {
    //paginacion filtrada por fechas
    Page<Egreso> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
}
