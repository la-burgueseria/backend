package com.example.laburgueseriabackend.service;

import com.example.laburgueseriabackend.model.entity.Insumo;

public interface IInsumo {

    Insumo save(Insumo insumo);
    Insumo findById(Integer id);
    void delete(Insumo insumo);
}
