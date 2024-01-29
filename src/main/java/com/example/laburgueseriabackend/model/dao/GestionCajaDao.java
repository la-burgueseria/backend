package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.GestionCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface GestionCajaDao extends JpaRepository<GestionCaja, Integer> {
    //buscar gestion caja entre dos fechas
    //la fecha inicio siempre debe de iniciar a las 12:00p.m del dia actual
    //y la fecha fin siempre debe terminar al dia siguiente a las 11:59 a.m.
    @Query("SELECT gc FROM GestionCaja gc WHERE gc.fechaHorainicio >= :fechaInicio AND gc.fechaHorainicio <= " +
            ":fechaFin ORDER BY gc.fechaHorainicio ASC")
    List<GestionCaja> findGestionCajaByFechaHorainicio(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
