package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.EmpleadoDto;
import com.example.laburgueseriabackend.model.entity.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEmpleadoService {

    Empleado save(EmpleadoDto empleadodto);
    Empleado findById(Integer id);
    void delete(Empleado empleado);
    Boolean existsById(Integer id);
    List<Empleado> listAll();
    List<Empleado> findEmpleadosByNombre(String nombre);
    Empleado findEmpleadoByDocumento(String documento);
    Page<Empleado> empleadosPaginados(Pageable pageable);
    Empleado actualizarEstado(Boolean estado, Integer id);
}
