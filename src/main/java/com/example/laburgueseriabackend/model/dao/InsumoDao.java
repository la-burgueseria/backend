package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.Insumo;
import org.springframework.data.repository.CrudRepository;

//interface extiende todo el metodo del crud de la clase CrudRepository
//recibe la clase de nuestra Entity y el tipo de dato del ID
// en caso de necesitar hacer paginacion
//se extiende la clase PagingAndSortingRepository<>
//esta tiene dos metodos, uno de organizar los registros y de paginar los registros
public interface InsumoDao extends CrudRepository<Insumo, Integer> {
}
