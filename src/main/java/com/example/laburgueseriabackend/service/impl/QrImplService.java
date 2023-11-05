package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.QrDao;
import com.example.laburgueseriabackend.model.dto.QrDto;
import com.example.laburgueseriabackend.model.entity.Qr;
import com.example.laburgueseriabackend.service.IQrService;
import jakarta.persistence.Id;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QrImplService implements IQrService {
    @Autowired
    private QrDao qrDao;
    //GUARDAR QR
    @Transactional
    @Override
    public Qr save(@NotNull QrDto qrDto) {
        Qr qr = Qr.builder()
                .id(qrDto.getId())
                .ruta(qrDto.getRuta())
                .url(qrDto.getUrl())
                .build();

        return qrDao.save(qr);
    }
    //BUSCAR POR ID
    @Transactional(readOnly = true)
    @Override
    public Qr findById(Integer id) {
        return qrDao.findById(id).orElse(null);
    }
    //ELIMINAR
    @Transactional
    @Override
    public void delete(Qr qr) {
        qrDao.delete(qr);
    }
    //LISTAR TODOS LOS QR
    @Transactional(readOnly = true)
    @Override
    public List<Qr> listAll() {
        return (List<Qr>) qrDao.findAll();
    }
    //BUSCAR POR RUTA
    @Transactional(readOnly = true)
    @Override
    public Qr findByRuta(String url) {
        return qrDao.findByRuta(url);
    }

    @Override
    public Boolean existsById(Integer id) {
        return qrDao.existsById(id);
    }
}
