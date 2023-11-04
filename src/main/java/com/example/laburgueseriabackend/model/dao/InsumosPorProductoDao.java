package com.example.laburgueseriabackend.model.dao;


import com.example.laburgueseriabackend.model.entity.InsumosPorProducto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InsumosPorProductoDao extends CrudRepository<InsumosPorProducto, Integer> {

    //validar ssi ya existe un insumo X asignado a un producto X
    //un producto no puede tener más de una vez el mismo insumo vinculado
    @Query("SELECT ipp FROM InsumosPorProducto ipp WHERE ipp.insumo.id = :idInsumo AND ipp.producto.id = :idProducto")
    List<InsumosPorProducto> insumoPorProductoExists(Integer idInsumo, Integer idProducto);
}
