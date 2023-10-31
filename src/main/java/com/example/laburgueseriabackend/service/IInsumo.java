package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.dto.InsumoDto;
import com.example.laburgueseriabackend.model.entity.Insumo;

public interface IInsumo {

    Insumo save(InsumoDto insumo);
    Insumo findById(Integer id);
    void delete(Insumo insumo);
}
