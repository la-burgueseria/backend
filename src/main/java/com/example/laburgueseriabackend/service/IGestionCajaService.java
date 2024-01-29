package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.GestionCajaDto;
import com.example.laburgueseriabackend.model.entity.GestionCaja;

import java.time.LocalDateTime;
import java.util.List;

public interface IGestionCajaService {
    GestionCaja save(GestionCajaDto gestionCajaDto);
    GestionCaja findById(Integer id);
    void delete(GestionCaja gestionCaja);
    Boolean existsById(Integer id);
    List<GestionCaja> listAll();
    List<GestionCaja> findGestionCajaByFechaHorainicio(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
