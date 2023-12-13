package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.CuentaProductosDto;
import com.example.laburgueseriabackend.model.entity.CuentaProductos;

import java.util.List;

public interface ICuentaProductosService {
    CuentaProductos save(CuentaProductosDto cuentaProductosDto);
    CuentaProductos findById(Integer id);
    void delete(CuentaProductos cuentaProductos);
    List<CuentaProductos> listAll();
    Boolean existsById(Integer id);
    List<CuentaProductos> getCuentaProductosByCuenta(Integer cuentaId);
}
