package com.example.laburgueseriabackend.model.dao;

import com.example.laburgueseriabackend.model.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

//interface extiende todo el metodo del crud de la clase CrudRepository
//recibe la clase de nuestra Entity y el tipo de dato del ID
// en caso de necesitar hacer paginacion
//se extiende la clase PagingAndSortingRepository<>
//esta tiene dos metodos, uno de organizar los registros y de paginar los registros
public interface InsumoDao extends JpaRepository<Insumo, Integer> {

    //buscar insumo por nombre
    //para que al momento de crear no hayan dos repetidos
    @Query("SELECT i FROM Insumo  i WHERE i.nombre = :nombre")
    Insumo findByNombre(String nombre);
    //selecionar insumos con similitud con el string enviado en la columna nombre
    @Query("SELECT i FROM Insumo i WHERE i.nombre LIKE %:nombre% ")
    List<Insumo> findInsumoByNameContaining(String nombre);
}
