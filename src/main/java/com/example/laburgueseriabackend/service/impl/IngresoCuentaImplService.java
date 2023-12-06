package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.IngresoCuentaDao;
import com.example.laburgueseriabackend.model.dto.IngresoCuentaDto;
import com.example.laburgueseriabackend.model.entity.IngresoCuenta;
import com.example.laburgueseriabackend.service.IIngresoCuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngresoCuentaImplService implements IIngresoCuentaService {
    @Autowired
    private IngresoCuentaDao ingresoCuentaDao;
    @Override
    public IngresoCuenta save(IngresoCuentaDto ingresoCuentaDto) {
        IngresoCuenta ingresoCuenta = IngresoCuenta.builder()
                .id(ingresoCuentaDto.getId())
                .ingreso(ingresoCuentaDto.getIngreso())
                .cuenta(ingresoCuentaDto.getCuenta())
                .build();

        return ingresoCuentaDao.save(ingresoCuenta);
    }

    @Override
    public IngresoCuenta findById(Integer id) {
        return ingresoCuentaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(IngresoCuenta ingresoCuenta) {
        ingresoCuentaDao.delete(ingresoCuenta);
    }

    @Override
    public List<IngresoCuenta> listAll() {
        return ingresoCuentaDao.findAll();
    }

    @Override
    public List<IngresoCuenta> obtenerCuentasPorIngreso(Integer ingresoId) {
        return (List<IngresoCuenta>) ingresoCuentaDao.obtenerCuentasPorIngreso(ingresoId);
    }
}
