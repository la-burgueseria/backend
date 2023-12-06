package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.IngresoCuentaDto;
import com.example.laburgueseriabackend.model.entity.IngresoCuenta;

import java.util.List;

public interface IIngresoCuentaService {

    IngresoCuenta save(IngresoCuentaDto ingresoCuentaDto);

    IngresoCuenta findById(Integer id);
    void delete(IngresoCuenta ingresoCuenta);
    List<IngresoCuenta> listAll();

    List<IngresoCuenta> obtenerCuentasPorIngreso(Integer ingresoId);
}
