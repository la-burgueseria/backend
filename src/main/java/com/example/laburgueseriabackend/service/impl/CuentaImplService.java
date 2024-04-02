package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.CuentaDao;
import com.example.laburgueseriabackend.model.dto.CuentaDto;
import com.example.laburgueseriabackend.model.entity.Cuenta;
import com.example.laburgueseriabackend.model.entity.Producto;
import com.example.laburgueseriabackend.service.ICuentaService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
                .total(cuentaDto.getTotal())
                .abono(cuentaDto.getAbono())
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

    @Override
    public Page<Cuenta> cuentasPaginadas(Pageable pageable) {
        return cuentaDao.findAll(pageable);
    }

    //devuelve las cuentas comprendidas en dicho rango de fechas
    @Override
    public List<Cuenta> getcuentasByFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return cuentaDao.getcuentasByFecha(fechaInicio, fechaFin);
    }
    //devuelve todas las cuentas en un rango de fechas vinculadas a un empleado dado.
    @Override
    public List<Cuenta> getCuentasByEmpleado(Integer empleadoId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return cuentaDao.getCuentasByEmpleado(empleadoId, fechaInicio, fechaFin);
    }
}
