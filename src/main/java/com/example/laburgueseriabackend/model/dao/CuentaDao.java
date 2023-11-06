package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.Cuenta;
import org.springframework.data.repository.CrudRepository;

public interface CuentaDao extends CrudRepository<Cuenta, Integer> {
}
