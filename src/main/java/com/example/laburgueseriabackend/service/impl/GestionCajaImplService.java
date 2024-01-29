package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.GestionCajaDao;
import com.example.laburgueseriabackend.model.dto.GestionCajaDto;
import com.example.laburgueseriabackend.model.entity.GestionCaja;
import com.example.laburgueseriabackend.service.IGestionCajaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GestionCajaImplService implements IGestionCajaService {
    @Autowired
    private GestionCajaDao gestionCajaDao;

    @Override
    public GestionCaja save(GestionCajaDto gestionCajaDto) {

        GestionCaja gestionCaja = GestionCaja.builder()
                .id(gestionCajaDto.getId())
                .totalCalculado(gestionCajaDto.getTotalCalculado())
                .totalReportado(gestionCajaDto.getTotalReportado())
                .saldoInicioCajaMenor(gestionCajaDto.getSaldoInicioCajaMenor())
                .observaciones(gestionCajaDto.getObservaciones())
                .fechaHorainicio(gestionCajaDto.getFechaHorainicio())
                .fechaHoraCierre(gestionCajaDto.getFechaHoraCierre())
                .estadoCaja(gestionCajaDto.getEstadoCaja())
                .build();

        return gestionCajaDao.save(gestionCaja);
    }

    @Override
    public GestionCaja findById(Integer id) {
        return gestionCajaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(GestionCaja gestionCaja) {
        gestionCajaDao.delete(gestionCaja);
    }

    @Override
    public Boolean existsById(Integer id) {
        return gestionCajaDao.existsById(id);
    }

    @Override
    public List<GestionCaja> listAll() {
        return gestionCajaDao.findAll();
    }

    @Override
    public List<GestionCaja> findGestionCajaByFechaHorainicio(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return gestionCajaDao.findGestionCajaByFechaHorainicio(fechaInicio, fechaFin);
    }
}
