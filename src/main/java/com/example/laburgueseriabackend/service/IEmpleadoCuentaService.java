package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.EmpleadoCuentaDto;
import com.example.laburgueseriabackend.model.entity.EmpleadoCuenta;

import java.util.List;

public interface IEmpleadoCuentaService {
    EmpleadoCuenta save(EmpleadoCuentaDto empleadoCuentaDto);
    EmpleadoCuenta findById(Integer id);
    void delete(EmpleadoCuenta empleadoCuenta);
    Boolean existsById(Integer id);
    List<EmpleadoCuenta> listAll();
    List<EmpleadoCuenta> cuentasDeEmpleado(Integer id);
}
