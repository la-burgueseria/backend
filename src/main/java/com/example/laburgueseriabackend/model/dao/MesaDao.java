package com.example.laburgueseriabackend.model.dao;


import com.example.laburgueseriabackend.model.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MesaDao extends JpaRepository<Mesa, Integer> {

    @Query("SELECT m from Mesa m WHERE m.numeroMesa = :numeroMesa")
    Mesa findByNumMesa(Integer numeroMesa);

    @Query("SELECT m from Mesa m WHERE m.numeroMesa = :numero")
    List<Mesa> findNumeroMesa(Integer numero);
    @Query("SELECT m FROM Mesa m WHERE m.qr.id = :qrId")
    Mesa findByQrId(Integer qrId);
    @Transactional
    @Modifying()
    @Query("UPDATE Mesa m SET m.isOcupada = :isOcupada WHERE m.id = :id")
    void changeOcupacionMesa(Integer id, Boolean isOcupada);
}
