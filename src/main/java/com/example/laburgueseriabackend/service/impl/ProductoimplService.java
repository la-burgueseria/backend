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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ProductoimplService implements IProductoService {
    //instancia del dao
    @Autowired
    private ProductoDao productoDao;

    //GUARADR
    @Transactional
    @Override
    public Producto save(String nombre, Double precio, String descripcion, MultipartFile img) {

        String nombreImagen = StringUtils.cleanPath(img.getOriginalFilename());

        if(nombreImagen.contains("..")){
            System.out.println("not a valid file");
        }

        Producto producto = null;
        try {
            producto = Producto.builder()
                    .id(0)
                    .nombre(nombre)
                    .precio(precio)
                    .imagen(img.getBytes())
                    .descripcion(descripcion)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
