package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.CuentaDto;
import com.example.laburgueseriabackend.model.entity.Cuenta;

import java.util.List;

public interface ICuentaService {
    Cuenta save(CuentaDto cuentaDto);
    Cuenta findById(Integer id);
    void delete(Cuenta cuenta);
    List<Cuenta> listAll();

    Boolean existsById(Integer id);
}
