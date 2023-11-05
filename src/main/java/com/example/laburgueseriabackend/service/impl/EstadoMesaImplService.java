package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.EstadoMesaDao;
import com.example.laburgueseriabackend.model.dto.EstadoMesaDto;
import com.example.laburgueseriabackend.model.entity.EstadoMesa;
import com.example.laburgueseriabackend.service.IEstadoMesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoMesaImplService implements IEstadoMesaService {
    @Autowired
    private EstadoMesaDao estadoMesaDao;
    //GUARDAR
    @Override
    public EstadoMesa save(EstadoMesaDto estadoMesaDto) {
        EstadoMesa estadoMesa = EstadoMesa.builder()
                .id(estadoMesaDto.getId())
                .nombre(estadoMesaDto.getNombre())
                .build();

        return estadoMesaDao.save(estadoMesa);
    }
    //BURCAR POR ID
    @Override
    public EstadoMesa findById(Integer id) {
        return estadoMesaDao.findById(id).orElse(null);
    }
    //ELIMINAR
    @Override
    public void delete(EstadoMesa estadoMesa) {
        estadoMesaDao.delete(estadoMesa);
    }
    //buscar todos los estados de las mesas
    @Override
    public List<EstadoMesa> listAll() {
        return (List<EstadoMesa>) estadoMesaDao.findAll();
    }
    //BUSCAR POR NOMBRE DE ESTADO MESA
    @Override
    public EstadoMesa findByNombre(String nombre) {
        return estadoMesaDao.findByNombre(nombre);
    }
    //verificar si existe
    @Override
    public Boolean existsById(Integer id) {
        return estadoMesaDao.existsById(id);
    }
}
