package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.InsumoDto;
import com.example.laburgueseriabackend.model.entity.Insumo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IInsumoService {

    Insumo save(InsumoDto insumo);
    Insumo findById(Integer id);
    void delete(Insumo insumo);
    Insumo findByNombre(String nombre);
    Boolean existsById(Integer id);
    List<Insumo> listAll();
    List<Insumo> findInsumoByNameContaining(String nombre);
    public Page<Insumo> insumosPaginados(Pageable pageable);
}
