package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.CategoriaProductoDao;
import com.example.laburgueseriabackend.model.dto.CategoriaProductoDto;
import com.example.laburgueseriabackend.model.entity.CategoriaProducto;
import com.example.laburgueseriabackend.service.ICategoriaProductoService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//TODO
@Service
public class CategoriaProductoImpService implements ICategoriaProductoService {
    //enlace con el Dao
    @Autowired
    private CategoriaProductoDao categoriaProductoDao;

    //GUARDAR
    @Override
    public CategoriaProducto save(@NotNull CategoriaProductoDto categoriaProductoDto) {

        //construir nuestra entridad con el dto que entra a la funcion
        CategoriaProducto categoriaProducto = CategoriaProducto.builder()
                .id(categoriaProductoDto.getId())
                .nombre(categoriaProductoDto.getNombre())
                .build();
        //cuardar nuestro objeto en nuestra base de datos
        //a traves del DAO
        return categoriaProductoDao.save(categoriaProducto);
    }

    //BUSCAR POR ID
    @Override
    public CategoriaProducto findById(Integer id) {
        return categoriaProductoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(CategoriaProducto categoriaProducto) {
        categoriaProductoDao.delete(categoriaProducto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoriaProducto> listAll() {
        return (List<CategoriaProducto>) categoriaProductoDao.findAll();
    }

    @Transactional
    @Override
    public CategoriaProducto findByNombre(String nombre) {
        return categoriaProductoDao.findByNombre(nombre);
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean existsById(Integer id) {
        return categoriaProductoDao.existsById(id);
    }
}
