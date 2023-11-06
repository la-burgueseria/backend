package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.CuentaProductosDao;
import com.example.laburgueseriabackend.model.dto.CuentaProductosDto;
import com.example.laburgueseriabackend.model.entity.CuentaProductos;
import com.example.laburgueseriabackend.service.ICuentaProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaProductosImplService implements ICuentaProductosService {
    @Autowired
    private CuentaProductosDao cuentaProductosDao;
    @Override
    public CuentaProductos save(CuentaProductosDto cuentaProductosDto) {
        CuentaProductos cuentaProductos = CuentaProductos.builder()
                .id(cuentaProductosDto.getId())
                .cantidad(cuentaProductosDto.getCantidad())
                .cuenta(cuentaProductosDto.getCuenta())
                .producto(cuentaProductosDto.getProducto())
                .build();

        return cuentaProductosDao.save(cuentaProductos);
    }

    @Override
    public CuentaProductos findById(Integer id) {
        return cuentaProductosDao.findById(id).orElse(null);
    }

    @Override
    public void delete(CuentaProductos cuentaProductos) {
        cuentaProductosDao.delete(cuentaProductos);
    }

    @Override
    public List<CuentaProductos> listAll() {
        return (List<CuentaProductos>) cuentaProductosDao.findAll();
    }

    @Override
    public Boolean existsById(Integer id) {
        return cuentaProductosDao.existsById(id);
    }
}
