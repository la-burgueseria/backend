package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.EgresoDto;
import com.example.laburgueseriabackend.model.entity.Egreso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface IEgresoService {
    Egreso save(EgresoDto egresoDto);
    Egreso findById(Integer id);
    void delete(Egreso egreso);
    Boolean existsById(Integer id);
    List<Egreso> listAll();
    Page<Egreso> egresosPaginados(Pageable pageable);
    Page<Egreso> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
}
