package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.EstadoCuentaDto;
import com.example.laburgueseriabackend.model.entity.EstadoCuenta;

import java.util.List;

public interface IEstadoCuentaService {
    EstadoCuenta save(EstadoCuentaDto estadoCuentaDto);
    EstadoCuenta findById(Integer id);
    void delete(EstadoCuenta estadoCuenta);
    List<EstadoCuenta> listAll();
    EstadoCuenta findByNombre(String nombre);
    Boolean existsById(Integer id);
}
