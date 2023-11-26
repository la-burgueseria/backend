package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.InsumosPorProductoDao;
import com.example.laburgueseriabackend.model.dto.InsumosPorProductoDto;
import com.example.laburgueseriabackend.model.entity.Insumo;
import com.example.laburgueseriabackend.model.entity.InsumosPorProducto;
import com.example.laburgueseriabackend.model.entity.Producto;
import com.example.laburgueseriabackend.service.IInsumosPorProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InsumosPorProductoImplService implements IInsumosPorProductoService {
    @Autowired
    private InsumosPorProductoDao insumosPorProductoDao;
    @Override
    public InsumosPorProducto save(InsumosPorProductoDto insumosPorProductoDto) {

        Insumo insumo = Insumo.builder()
                .id(insumosPorProductoDto.getInsumoDto().getId())
                .build();
        Producto producto= Producto.builder()
                .id(insumosPorProductoDto.getProductoDto().getId())
                .build();

        InsumosPorProducto insumosPorProducto = InsumosPorProducto.builder()
                .id(insumosPorProductoDto.getId())
                .insumo(
                      insumo
                )
                .producto(
                        producto
                )
                .cantidad(insumosPorProductoDto.getCantidad())
                .build();


        return insumosPorProductoDao.save(insumosPorProducto);
    }

    @Transactional(readOnly = true)
    @Override
    public InsumosPorProducto findById(Integer id) {
        return insumosPorProductoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(InsumosPorProducto insumosPorProducto) {
        insumosPorProductoDao.delete(insumosPorProducto);
    }

    @Override
    public List<InsumosPorProducto> listAll() {
        return (List) insumosPorProductoDao.findAll();
    }

    @Override
    public Boolean existsById(Integer id) {
        return insumosPorProductoDao.existsById(id);
    }

    @Override
    public List<InsumosPorProducto> insumoPorProductoExists(Integer idInsumo, Integer idProducto) {
        return (List<InsumosPorProducto>) insumosPorProductoDao.insumoPorProductoExists(idInsumo, idProducto);
    }

    @Override
    public List<InsumosPorProducto> selecionarInsumosDelProducto(Integer idProducto) {
        return (List<InsumosPorProducto>) insumosPorProductoDao.selecionarInsumosDelProducto(idProducto);
    }
}
