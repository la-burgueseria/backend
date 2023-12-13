package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CuentaDao extends JpaRepository<Cuenta, Integer> {
}
