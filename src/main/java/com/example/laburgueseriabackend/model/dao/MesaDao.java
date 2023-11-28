package com.example.laburgueseriabackend.model.dao;


import com.example.laburgueseriabackend.model.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MesaDao extends JpaRepository<Mesa, Integer> {

    @Query("SELECT m from Mesa m WHERE m.numeroMesa = :numeroMesa")
    Mesa findByNumMesa(Integer numeroMesa);

    @Query("SELECT m FROM Mesa m WHERE m.qr.id = :qrId")
    Mesa findByQrId(Integer qrId);
}
