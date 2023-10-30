package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.InsumoDao;
import com.example.laburgueseriabackend.model.entity.Insumo;
import com.example.laburgueseriabackend.service.IInsumo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//se implementa la interfaz donde se declararon los metodos
//se especifica que esta clase sera un service
//se implementa el Dao para vincular nuestros metodos con los dclarados en el Dao
@Service
public class Insumoimpl implements IInsumo {

    @Autowired
    private InsumoDao insumoDao;

    @Transactional
    @Override
    public Insumo save(Insumo insumo) {
        //implementacion del metodo de CrudRepository
        //el save() tiene dos funciones = guardar registro y actualizar registro
        return insumoDao.save(insumo);
    }

    @Transactional(readOnly = true)
    @Override
    public Insumo findById(Integer id) {
        //JPA tiene un metodo en el caso de buscar registros
        //.orElse() permite retornar un valor distinto en caso de que el registro no se encontrado
        return insumoDao.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void delete(Insumo insumo) {
        insumoDao.delete(insumo);
    }
}
