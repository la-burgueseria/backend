package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.InsumoDto;
import com.example.laburgueseriabackend.model.entity.Insumo;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.IInsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //especificar que esta clase es un controller
@RequestMapping("/api/v1") //asignacion de la ruta para ser consumido
public class InsumoController {

    @Autowired //inyeccion de dependencias del servicio
    private IInsumoService insumoService;

    //LISTAR TODOS LOS INSUMOS
    @GetMapping("insumos")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){
        List<Insumo> insumos = insumoService.listAll();

        if(insumos == null){
            return new ResponseEntity<>(MensajeResponse
                    .builder()
                    .mensaje("No hay registros en el sistema")
                    .object(null)
                    .build()
                    , HttpStatus.OK);
        }

        return new ResponseEntity<>(MensajeResponse
                .builder()
                .mensaje("OK")
                .object(insumos)
                .build()
                , HttpStatus.OK
        );
    }


    //CREAR INSUMO
    @PostMapping("insumo") //rutaa para lanzar el evento post
    @ResponseStatus(HttpStatus.CREATED) //asignar códigos de respuesta http
    public ResponseEntity<?> create(@RequestBody InsumoDto insumoDto){ //request body transforma el json enviado al objeto necesitado

        Insumo insumoSave = null;
        Insumo insumoExists = null;
        try{
            //validar si un insumo del mismo nombre ya se encuentra registrado
            insumoExists = insumoService.findByNombre(insumoDto.getNombre());
            //en caso de que exista
            if(insumoExists != null){
                Integer cantidadExistencia = insumoExists.getCantidad() + insumoDto.getCantidad();

                insumoExists.setCantidad(cantidadExistencia);
                Integer id1 = insumoDto.getId();
                Integer id2 = insumoExists.getId();


                insumoDto =  InsumoDto.builder()
                        .id(insumoExists.getId())
                        .nombre(insumoExists.getNombre())
                        .cantidad(insumoExists.getCantidad())
                        .build();

                insumoSave = insumoService.save(insumoDto);

                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Registro ya existia anteriormente, se han sumado las existencias Id del insumo enviado por body: " + id1
                                + " Id del insumo consultado: " + id2)
                        .object(insumoDto)
                        .build(), HttpStatus.CREATED) ;
            }
            else{
                insumoSave = insumoService.save(insumoDto);

                insumoDto =  InsumoDto.builder()
                        .id(insumoSave.getId())
                        .nombre(insumoSave.getNombre())
                        .cantidad(insumoSave.getCantidad())
                        .build();

                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Guardado correctamente")
                        .object(insumoDto)
                        .build(), HttpStatus.CREATED) ;
            }


        }catch(DataAccessException exDt){
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje(exDt.getMessage())
                    .object(null)
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }


    //ACTUALIZAR INSUMO
    @PutMapping("insumo/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody InsumoDto insumoDto, @PathVariable Integer id){
        Insumo insumoUpdate = null;

        try{

            if(insumoService.existsById(insumoDto.getId())){
                insumoDto.setId(id);
                insumoUpdate = insumoService.save(insumoDto);

                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Actualizado correctamente")
                        .object(InsumoDto.builder()
                                .id(insumoUpdate.getId())
                                .nombre(insumoUpdate.getNombre())
                                .cantidad(insumoUpdate.getCantidad())
                                .build())
                        .build()
                        , HttpStatus.CREATED
                );
            }else{
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Registro no encontrado en la base de datos")
                        .object(null)
                        .build()
                        , HttpStatus.NOT_FOUND
                );
            }
        }catch(DataAccessException exDt){
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje(exDt.getMessage())
                    .object(null)
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    //ELIMINAR INSUMO
    @DeleteMapping("insumo/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){ //recibe el parametro enviado por url
    //ResponseEntity maneja toda la respuesta HTTP incluyendo el cuerpo
    //cabecera y códigos de estado lo que permite libertad para configurar la respuesta enviada

        try{
            Insumo insumoDelete = insumoService.findById(id);
            insumoService.delete(insumoDelete);
            return new ResponseEntity<>(insumoDelete, HttpStatus.NO_CONTENT);
        }catch(DataAccessException exDt){

            return new ResponseEntity<>(MensajeResponse
                        .builder()
                        .mensaje(exDt.getMessage())
                        .object(null)
                        .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("insumo/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showById(@PathVariable Integer id){
        Insumo insumo =  insumoService.findById(id);

        if(insumo == null){
            return new ResponseEntity<>(MensajeResponse
                    .builder()
                    .mensaje("Registro no encontrado")
                    .object(null)
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }

        InsumoDto insumoDto =  InsumoDto.builder()
                .id(insumo.getId())
                .nombre(insumo.getNombre())
                .cantidad(insumo.getCantidad())
                .build();

        return new ResponseEntity<>(MensajeResponse
                .builder()
                .mensaje("OK")
                .object(insumoDto)
                .build()
                , HttpStatus.OK
        );
    }
}
