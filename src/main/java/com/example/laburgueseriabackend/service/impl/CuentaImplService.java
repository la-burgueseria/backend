package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.CuentaDao;
import com.example.laburgueseriabackend.model.dto.CuentaDto;
import com.example.laburgueseriabackend.model.entity.Cuenta;
import com.example.laburgueseriabackend.model.entity.Producto;
import com.example.laburgueseriabackend.service.ICuentaService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CuentaImplService implements ICuentaService {
    @Autowired
    private CuentaDao cuentaDao;
    @Override
    public Cuenta save(@NotNull CuentaDto cuentaDto) {



        Cuenta cuenta = Cuenta.builder()
                .id(cuentaDto.getId())
                .fecha(cuentaDto.getFecha())
                .estadoCuenta(cuentaDto.getEstadoCuenta())
                .mesa(cuentaDto.getMesa())
                .build();

        return cuentaDao.save(cuenta);
    }

    @Override
    public Cuenta findById(Integer id) {
        return cuentaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Cuenta cuenta) {
        cuentaDao.delete(cuenta);
    }

    @Override
    public List<Cuenta> listAll() {
        return (List<Cuenta>) cuentaDao.findAll();
    }

    @Override
    public Boolean existsById(Integer id) {
        return cuentaDao.existsById(id);
    }
}
