package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.EgresoDao;
import com.example.laburgueseriabackend.model.dto.EgresoDto;
import com.example.laburgueseriabackend.model.entity.Egreso;
import com.example.laburgueseriabackend.service.IEgresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EgresoImplService implements IEgresoService {
    @Autowired
    private EgresoDao egresoDao;

    @Override
    public Egreso save(EgresoDto egresoDto) {
        Egreso egreso = Egreso.builder()
                .id(egresoDto.getId())
                .fecha(egresoDto.getFecha())
                .descripcion(egresoDto.getDescripcion())
                .categoria(egresoDto.getCategoria())
                .total(egresoDto.getTotal())
                .deduccionDesde(egresoDto.getDeduccionDesde())
                .build();
        return egresoDao.save(egreso);
    }

    @Override
    public Egreso findById(Integer id) {
        return egresoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Egreso egreso) {
        egresoDao.delete(egreso);
    }

    @Override
    public Boolean existsById(Integer id) {
        return egresoDao.existsById(id);
    }

    @Override
    public List<Egreso> listAll() {
        return egresoDao.findAll();
    }

    @Override
    public Page<Egreso> egresosPaginados(Pageable pageable) {
        return egresoDao.findAll(pageable);
    }

    @Override
    public Page<Egreso> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable) {
        return egresoDao.findByFechaBetween(fechaInicio, fechaFin, pageable);
    }

    @Override
    public List<Egreso> findEgresoByFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return egresoDao.findEgresoByFechas(fechaInicio, fechaFin);
    }
}
