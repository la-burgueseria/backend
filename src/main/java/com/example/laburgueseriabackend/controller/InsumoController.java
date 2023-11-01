package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.InsumoDto;
import com.example.laburgueseriabackend.model.entity.Insumo;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.IInsumo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController //especificar que esta clase es un controller
@RequestMapping("/api/v1") //asignacion de la ruta para ser consumido
public class InsumoController {

    @Autowired //inyeccion de dependencias del servicio
    private IInsumo insumoService;


    //CREAR INSUMO
    @PostMapping("insumo") //rutaa para lanzar el evento post
    @ResponseStatus(HttpStatus.CREATED) //asignar códigos de respuesta http
    public ResponseEntity<?> create(@RequestBody InsumoDto insumoDto){ //request body transforma el json enviado al objeto necesitado

        Insumo insumoSave = null;
        try{
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
            Insumo findInsumo = insumoService.findById(id);

            if(findInsumo != null){
                insumoUpdate = insumoService.save(insumoDto);

                insumoDto =  InsumoDto.builder()
                        .id(insumoUpdate.getId())
                        .nombre(insumoUpdate.getNombre())
                        .cantidad(insumoUpdate.getCantidad())
                        .build();
            }
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Actualizado correctamente")
                    .object(insumoDto)
                    .build(), HttpStatus.CREATED) ;

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
                , HttpStatus.FOUND  
        );


    }
}
