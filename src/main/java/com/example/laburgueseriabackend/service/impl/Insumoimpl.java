package com.example.laburgueseriabackend.service.impl;

import com.example.laburgueseriabackend.model.dao.InsumoDao;
import com.example.laburgueseriabackend.model.dto.InsumoDto;
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
    public Insumo save(InsumoDto insumoDto) {
        // Al usar los DTO evitamos que nuestra entity
        //este directamente expuesta al response body por lo cual nos brinda mayor seguridad
        // por lo cual usamos el DTO y luego con la información del dto creamos nuestro
        //"VERDADERO" modelo
        Insumo insumo = Insumo.builder()
                .id(insumoDto.getId())
                .nombre(insumoDto.getNombre())
                .cantidad(insumoDto.getCantidad())
                .build();
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
        //se elimina el insumo
        insumoDao.delete(insumo);
    }

    @Transactional
    @Override
    public Insumo buscarInsumoByNombre(Insumo){
        //busca un registro con el mismo nombre
        return insumoDao.buscarInsumoByNombre();
    }
}
