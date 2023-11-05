package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.MesaDao;
import com.example.laburgueseriabackend.model.dto.MesaDto;
import com.example.laburgueseriabackend.model.entity.EstadoMesa;
import com.example.laburgueseriabackend.model.entity.Mesa;
import com.example.laburgueseriabackend.model.entity.Qr;
import com.example.laburgueseriabackend.service.IMesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaImplService implements IMesaService {
    @Autowired
    private MesaDao mesaDao;
    //GUARDAR MESA
    @Override
    public Mesa save(MesaDto mesaDto) {
        Mesa mesa = Mesa.builder()
                .id(mesaDto.getId())
                .numeroMesa(mesaDto.getNumeroMesa())
                .qr(
                        Qr.builder()
                                .id(mesaDto.getQr().getId())
                                .ruta(mesaDto.getQr().getRuta())
                                .url(mesaDto.getQr().getUrl())
                                .build()
                )
                .estadoMesa(
                        EstadoMesa.builder()
                                .id(mesaDto.getEstadoMesa().getId())
                                .nombre(mesaDto.getEstadoMesa().getNombre())
                                .build()
                )
                .build();
        return mesaDao.save(mesa);
    }
    //BUSCAR MESA POR ID
    @Override
    public Mesa findById(Integer id) {
        return mesaDao.findById(id).orElse(null);
    }
    //ELIMINAR MESA
    @Override
    public void delete(Mesa mesa) {
        mesaDao.delete(mesa);
    }
    //VERIFICAR SI YA EXISTE UNA MESA CON EL MISMO NUMERO
    @Override
    public Mesa findByNumMesa(Integer numeroMesa) {
        return mesaDao.findByNumMesa(numeroMesa);
    }

    @Override
    public Mesa findByQrId(Integer qrId) {
        return mesaDao.findByQrId(qrId);
    }

    //LISTAR TODOS
    @Override
    public List<Mesa> listAll() {
        return (List<Mesa>) mesaDao.findAll();
    }
    //VERIFICAR SI EXISTE EL ID
    @Override
    public Boolean existsById(Integer id) {
        return mesaDao.existsById(id);
    }
}
