package com.example.laburgueseriabackend.controller;


import com.example.laburgueseriabackend.model.dto.CategoriaProductoDto;
import com.example.laburgueseriabackend.model.dto.ProductoDto;
import com.example.laburgueseriabackend.model.entity.CategoriaProducto;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.ICategoriaProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

//CONTROLLER CATEGORIAS DE PRODUCTOS
@RestController
@RequestMapping("/api/v1")
public class CategoriaProductoController {

    @Autowired //implementacion del servicio con nuestros métodos crud
    private ICategoriaProductoService categoriaProductoService;

    //LISTAR TODAS LAS CATEGORIAS
    @GetMapping("categorias-productos")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){

        try{
            List<CategoriaProducto> categoriasProductos = categoriaProductoService.listAll();
            //en caso de no haber registros
            if(categoriasProductos == null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("No existen registros actualmente")
                                .object(null)
                                .build()
                        , HttpStatus.OK
                );
            }
            //En caso de haber registros retorna la lista
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ok")
                            .object(categoriasProductos)
                            .build()
                    , HttpStatus.OK
            );
        }catch (DataAccessException exDt){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }



    }

    //CREAR NUEVA CATEGORIA
    @PostMapping("categoria-producto")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody CategoriaProductoDto categoriaProductoDto){

        CategoriaProducto categoriaProductoSave = null;
        CategoriaProducto categoriaProductoExists = null;

        try{
            //consultar si ya existe un registro con el mismo nombre
            categoriaProductoExists = categoriaProductoService.findByNombre(categoriaProductoDto.getNombre());

            //en caso de existir
            if(categoriaProductoExists != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ya existe una categoría con el mismo nombre")
                                .object(null)
                                .build()
                                , HttpStatus.OK
                );
            }
            //en caso de que no exista se guarda
            categoriaProductoSave = categoriaProductoService.save(categoriaProductoDto);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Categoria creada con exito")
                            .object(CategoriaProductoDto.builder()
                                    .id(categoriaProductoSave.getId())
                                    .nombre(categoriaProductoSave.getNombre())
                                    .build()
                            )
                            .build()
                    , HttpStatus.CREATED
            );
        }catch (DataAccessException exDt){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // ACTUALIZAR CATEGORIA
    @PutMapping("categoria-producto/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody CategoriaProductoDto categoriaProductoDto, Integer id){
        CategoriaProducto categoriaProductoUpdate = null;

        try{
            //validar si el registro existe
            if(categoriaProductoService.existsById(id)){
                //asignamos el id enviado por header
                //para asegurar que se trate del mismo ID
                categoriaProductoDto.setId(id);

                categoriaProductoUpdate = categoriaProductoService.save(categoriaProductoDto);

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Registro actualizado exitosamente")
                                .object(
                                        CategoriaProductoDto.builder()
                                                .id(categoriaProductoUpdate.getId())
                                                .nombre(categoriaProductoUpdate.getNombre())
                                                .build()
                                )
                                .build()
                        , HttpStatus.CREATED
                );
            }
            //Si no existe:
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No se ha encontrado el registro")
                            .object(null)
                            .build()
                    , HttpStatus.NOT_FOUND
            );

        }catch (DataAccessException exDt){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    //ELIMINAR CATEGORIA
    @DeleteMapping("categoria-producto/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        try{
            //validar si el registro existe
            if(categoriaProductoService.existsById(id)){
                //en caso de existir
                CategoriaProducto categoriaProductoDelete = categoriaProductoService.findById(id);
                categoriaProductoService.delete(categoriaProductoDelete);

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El registro ha sido eliminado exitosamente")
                                .object(null)
                                .build()
                        , HttpStatus.NO_CONTENT
                );
            }
            //en caso de NO existir
            return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("El registro que se intenta eliminar no existe.")
                        .object(null)
                        .build()
                    , HttpStatus.NOT_FOUND
            );
        }catch (DataAccessException exDt){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // BUSCAR CATEGORIA POR ID
    @Transactional(readOnly = true)
    @GetMapping("categoria-producto/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<?> showById(@PathVariable Integer id){
        try{
            CategoriaProducto categoriaProducto = categoriaProductoService.findById(id);

            if(categoriaProducto != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ok, encontrado")
                                .object(
                                        CategoriaProductoDto.builder()
                                                .id(categoriaProducto.getId())
                                                .nombre(categoriaProducto.getNombre())
                                                .productos(categoriaProducto.getProductos())
                                                .build()
                                )
                                .build()
                        , HttpStatus.OK
                );
            }
            return new ResponseEntity<>(MensajeResponse
                    .builder()
                    .mensaje("Registro no encontrado")
                    .object(null)
                    .build()
                    , HttpStatus.NOT_FOUND);

        }catch (DataAccessException exdt){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exdt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}
