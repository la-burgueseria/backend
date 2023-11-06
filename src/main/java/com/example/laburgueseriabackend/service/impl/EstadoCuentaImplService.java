package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.EstadoCuentaDao;
import com.example.laburgueseriabackend.model.dto.EstadoCuentaDto;
import com.example.laburgueseriabackend.model.entity.EstadoCuenta;
import com.example.laburgueseriabackend.service.IEstadoCuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoCuentaImplService implements IEstadoCuentaService {
    @Autowired
    private EstadoCuentaDao estadoCuentaDao;
    @Override
    public EstadoCuenta save(EstadoCuentaDto estadoCuentaDto) {

        EstadoCuenta estadoCuenta = EstadoCuenta.builder()
                .id(estadoCuentaDto.getId())
                .nombre(estadoCuentaDto.getNombre())
                .build();

        return estadoCuentaDao.save(estadoCuenta);
    }

    @Override
    public EstadoCuenta findById(Integer id) {
        return estadoCuentaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(EstadoCuenta estadoCuenta) {
        estadoCuentaDao.delete(estadoCuenta);
    }

    @Override
    public List<EstadoCuenta> listAll() {
        return (List<EstadoCuenta>) estadoCuentaDao.findAll();
    }

    @Override
    public EstadoCuenta findByNombre(String nombre) {
        return estadoCuentaDao.findByNombre(nombre);
    }

    @Override
    public Boolean existsById(Integer id) {
        return estadoCuentaDao.existsById(id);
    }
}
