package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.IngresoDto;
import com.example.laburgueseriabackend.model.entity.Ingreso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface IIngresoService {

    Ingreso save(IngresoDto ingresoDto);
    Ingreso findById(Integer id);
    void delete(Ingreso ingreso);
    Boolean existsById(Integer id);
    List<Ingreso> listAll();
    Page<Ingreso> ingresosPaginados(Pageable pageable);

    List<Ingreso> findIngresoByFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    //filtra por fecha y devuelve paginacion
    Page<Ingreso> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
}
