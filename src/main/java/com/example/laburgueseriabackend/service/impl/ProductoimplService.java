package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.ProductoDao;
import com.example.laburgueseriabackend.model.dto.CategoriaProductoDto;
import com.example.laburgueseriabackend.model.dto.ProductoDto;
import com.example.laburgueseriabackend.model.entity.CategoriaProducto;
import com.example.laburgueseriabackend.model.entity.Producto;
import com.example.laburgueseriabackend.service.IProductoService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.*;
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
    public Producto save(String nombre, Double precio, String descripcion, MultipartFile img, Integer categoriaId) {
        Producto producto = null;
        try {
            if(img == null){
                producto = Producto.builder()
                        .id(0)
                        .nombre(nombre)
                        .precio(precio)
                        .imagen(null)
                        .descripcion(descripcion)
                        .categoriaProducto(
                                CategoriaProducto.builder()
                                        .id(categoriaId)
                                        .nombre("")
                                        .build()
                        )
                        .build();
            }else{
                producto = Producto.builder()
                        .id(0)
                        .nombre(nombre)
                        .precio(precio)
                        .imagen(img.getBytes())
                        .descripcion(descripcion)
                        .categoriaProducto(
                                CategoriaProducto.builder()
                                        .id(categoriaId)
                                        .nombre("")
                                        .build()
                        )
                        .build();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return productoDao.save(producto);
    }
    @Transactional
    @Override
    public Producto save2(String nombre, Double precio, String descripcion, MultipartFile img, Integer categoriaId, Integer id) {
        Producto producto = null;
        try {
            if(img == null){
                producto = Producto.builder()
                        .id(id)
                        .nombre(nombre)
                        .precio(precio)
                        .imagen(null)
                        .descripcion(descripcion)
                        .categoriaProducto(
                                CategoriaProducto.builder()
                                        .id(categoriaId)
                                        .nombre("")
                                        .build()
                        )
                        .build();
            }else{
                //optimizar imagen antes de guardarla
                byte[] imagenBytes = img.getBytes();

                byte[] imagenOptimizada = optimizarImagen(imagenBytes);

                producto = Producto.builder()
                        .id(id)
                        .nombre(nombre)
                        .precio(precio)
                        .imagen(img.getBytes())
                        .descripcion(descripcion)
                        .categoriaProducto(
                                CategoriaProducto.builder()
                                        .id(categoriaId)
                                        .nombre("")
                                        .build()
                        )
                        .build();
            }


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

    //paginación de productos
    @Override
    public Page<Producto> productosPaginados(Pageable pageable) {
        return productoDao.findAll(pageable);
    }
    //busqueda por nombre
    @Override
    public List<Producto> findProductoByNombre(String nombre) {
        return (List<Producto>) productoDao.findProductoByNombre(nombre);
    }

    private byte[] optimizarImagen(byte[] imagenOriginal){
            try{
                InputStream inputStream = new ByteArrayInputStream(imagenOriginal);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                Thumbnails.of(inputStream)
                        .size(300,200)
                        .outputFormat("jpg")
                        .toOutputStream(outputStream);

                byte[] imagenOptimizadaBytes = outputStream.toByteArray();
                FileOutputStream fos = new FileOutputStream("imagen.jpg");
                fos.write(imagenOptimizadaBytes);
                fos.close();
                // Verifica si el directorio existe, y si no, créalo
                File directorio = new File("img");
                if (!directorio.exists()) {
                    directorio.mkdirs();
                }


                return outputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                return imagenOriginal;
            }
    }
}
