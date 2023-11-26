package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.ProductoDto;
import com.example.laburgueseriabackend.model.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductoService {


    //GUARADR
    @Transactional
    Producto save(String nombre, Double precio, String descripcion, MultipartFile img, Integer categoriaId);
    @Transactional
    Producto save2(String nombre, Double precio, String descripcion, MultipartFile img, Integer categoriaId, Integer id);
    Producto findById(Integer id);
    void delete(Producto producto);
    Producto findByNombre(String nombre);

    List<Producto> listAll();

    Boolean existsById(Integer id);
    Page<Producto> productosPaginados(Pageable pageable);
    List<Producto> findProductoByNombre(String nombre);

}
