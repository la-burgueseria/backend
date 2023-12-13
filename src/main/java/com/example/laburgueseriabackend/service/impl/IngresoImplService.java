package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.IngresoDao;
import com.example.laburgueseriabackend.model.dto.IngresoDto;
import com.example.laburgueseriabackend.model.entity.Ingreso;
import com.example.laburgueseriabackend.service.IIngresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IngresoImplService  implements IIngresoService {
    @Autowired
    private IngresoDao ingresoDao;

    @Override
    public Ingreso save(IngresoDto ingresoDto) {

        Ingreso ingreso = Ingreso.builder()
                .id(ingresoDto.getId())
                .fecha(ingresoDto.getFecha())
                .metodoPago(ingresoDto.getMetodoPago())
                .total(ingresoDto.getTotal())
                .cuenta(ingresoDto.getCuenta())
                .build();
        return ingresoDao.save(ingreso);
    }

    @Override
    public Ingreso findById(Integer id) {
        return ingresoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Ingreso ingreso) {
        ingresoDao.delete(ingreso);
    }

    @Override
    public Boolean existsById(Integer id) {
        return ingresoDao.existsById(id);
    }

    @Override
    public List<Ingreso> listAll() {
        return ingresoDao.findAll();
    }

    @Override
    public Page<Ingreso> ingresosPaginados(Pageable pageable) {
        return ingresoDao.findAll(pageable);
    }

    @Override
    public List<Ingreso> findIngresoByFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ingresoDao.findIngresoByFechas(fechaInicio, fechaFin);
    }
}
