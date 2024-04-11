package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.EmpleadoCuentaDao;
import com.example.laburgueseriabackend.model.dto.EmpleadoCuentaDto;
import com.example.laburgueseriabackend.model.entity.EmpleadoCuenta;
import com.example.laburgueseriabackend.service.IEmpleadoCuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoCuentaImplService implements IEmpleadoCuentaService {
   @Autowired
   private EmpleadoCuentaDao empleadoCuentaDao;
    @Override
    public EmpleadoCuenta save(EmpleadoCuentaDto empleadoCuentaDto) {

        EmpleadoCuenta empleadoCuenta = EmpleadoCuenta.builder()
                .id(empleadoCuentaDto.getId())
                .cuenta(empleadoCuentaDto.getCuenta())
                .empleado(empleadoCuentaDto.getEmpleado())
                .build();

        return empleadoCuentaDao.save(empleadoCuenta);
    }

    @Override
    public EmpleadoCuenta findById(Integer id) {
        return empleadoCuentaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(EmpleadoCuenta empleadoCuenta) {
        empleadoCuentaDao.delete(empleadoCuenta);
    }

    @Override
    public Boolean existsById(Integer id) {
        return empleadoCuentaDao.existsById(id);
    }

    @Override
    public List<EmpleadoCuenta> listAll() {
        return (List<EmpleadoCuenta>) empleadoCuentaDao.findAll();
    }

    @Override
    public List<EmpleadoCuenta> cuentasDeEmpleado(Integer id) {
        return (List<EmpleadoCuenta>) empleadoCuentaDao.cuentasDeEmpleado(id);
    }

    @Override
    public List<EmpleadoCuenta> empleadoEnCuenta(Integer id){
        return (List<EmpleadoCuenta>) empleadoCuentaDao.empleadoEnCuenta(id);
    }
}
