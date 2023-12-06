package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.EmpleadoDao;
import com.example.laburgueseriabackend.model.dto.EmpleadoDto;
import com.example.laburgueseriabackend.model.entity.Empleado;
import com.example.laburgueseriabackend.service.IEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpleadoImplService implements IEmpleadoService {

    @Autowired
    private EmpleadoDao empleadoDao;

    @Transactional
    @Override
    public Empleado save(EmpleadoDto empleadodto) {

        Empleado empleado = Empleado.builder()
                .id(empleadodto.getId())
                .nombre(empleadodto.getNombre())
                .apellido(empleadodto.getApellido())
                .documento(empleadodto.getDocumento())
                .estado(empleadodto.getEstado())
                .build();

        return empleadoDao.save(empleado);
    }

    @Transactional(readOnly = true)
    @Override
    public Empleado findById(Integer id) {
        return empleadoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Empleado empleado) {
        empleadoDao.delete(empleado);
    }

    @Override
    public Boolean existsById(Integer id) {
        return empleadoDao.existsById(id);
    }

    @Override
    public List<Empleado> listAll() {
        return (List<Empleado>) empleadoDao.findAll();
    }

    @Override
    public List<Empleado> findEmpleadosByNombre(String nombre) {
        return (List<Empleado>) empleadoDao.findEmpleadosByNombre(nombre);
    }

    @Override
    public Empleado findEmpleadoByDocumento(Long documento) {
        return this.empleadoDao.findEmpleadosByDocumento(documento);
    }

    @Override
    public Page<Empleado> empleadosPaginados(Pageable pageable) {
        return empleadoDao.findAll(pageable);
    }

    @Override
    public Empleado actualizarEstado(Boolean estado, Integer id) {
        return empleadoDao.actualizarEstado(estado, id);
    }
}
