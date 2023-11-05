package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.EstadoMesa;
import com.example.laburgueseriabackend.model.entity.Qr;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface QrDao extends CrudRepository<Qr, Integer> {

    @Query("SELECT q from Qr q WHERE q.url = :url")
    Qr findByRuta(String url);
}
