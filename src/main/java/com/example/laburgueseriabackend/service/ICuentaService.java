package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.CuentaDto;
import com.example.laburgueseriabackend.model.entity.Cuenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ICuentaService {
    Cuenta save(CuentaDto cuentaDto);
    Cuenta findById(Integer id);
    void delete(Cuenta cuenta);
    List<Cuenta> listAll();
    Boolean existsById(Integer id);
    Page<Cuenta> cuentasPaginadas(Pageable pageable);
    List<Cuenta> getcuentasByFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Cuenta> getCuentasByEmpleado(Integer empleadoId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
