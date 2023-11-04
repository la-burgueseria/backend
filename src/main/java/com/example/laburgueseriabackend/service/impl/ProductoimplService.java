package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.ProductoDao;
import com.example.laburgueseriabackend.model.dto.CategoriaProductoDto;
import com.example.laburgueseriabackend.model.dto.ProductoDto;
import com.example.laburgueseriabackend.model.entity.CategoriaProducto;
import com.example.laburgueseriabackend.model.entity.Producto;
import com.example.laburgueseriabackend.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoimplService implements IProductoService {
    //instancia del dao
    @Autowired
    private ProductoDao productoDao;

    //GUARADR
    @Transactional
    @Override
    public Producto save(ProductoDto productoDto) {
        //obtener la categoria enviada dentro del productoDto
        CategoriaProductoDto categoriaProductoDto = productoDto.getCategoriaProductoDto();
        //convertir de CategoriaProductoDto a CategoriaProducto
        CategoriaProducto categoriaProducto = CategoriaProducto.builder()
                .id(categoriaProductoDto.getId())
                .nombre(categoriaProductoDto.getNombre())
                .build();

        Producto producto = Producto.builder()
                .id(productoDto.getId())
                .nombre(productoDto.getNombre())
                .precio(productoDto.getPrecio())
                .imagen(productoDto.getImagen())
                .descripcion(productoDto.getDescripcion())
                .categoriaProducto(categoriaProducto)
                .build();

        return productoDao.save(producto);
    }

    //buscar por id
    @Transactional(readOnly = true)
    @Override
    public Producto findById(Integer id) {
        return productoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Producto producto) {
        productoDao.delete(producto);
    }

    @Transactional
    @Override
    public Producto findByNombre(String nombre) {
        return productoDao.findByNombre(nombre);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Producto> listAll() {
        return (List) productoDao.findAll();
    }

    @Override
    public Boolean existsById(Integer id) {
        return productoDao.existsById(id);
    }
}
