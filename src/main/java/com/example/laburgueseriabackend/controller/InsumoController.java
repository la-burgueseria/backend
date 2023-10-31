package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.InsumoDto;
import com.example.laburgueseriabackend.model.entity.Insumo;
import com.example.laburgueseriabackend.service.IInsumo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController //especificar que esta clase es un controller
@RequestMapping("/api/v1") //asignacion de la ruta para ser consumido
public class InsumoController {

    @Autowired //inyeccion de dependencias del servicio
    private IInsumo insumoService;

    @PostMapping("insumo") //rutaa para lanzar el evento post
    @ResponseStatus(HttpStatus.CREATED) //asignar códigos de respuesta http
    public InsumoDto create(@RequestBody InsumoDto insumoDto){ //request body transforma el json enviado al objeto necesitado
        Insumo insumoSave = insumoService.save(insumoDto);

        return InsumoDto.builder()
                .id(insumoSave.getId())
                .nombre(insumoSave.getNombre())
                .cantidad(insumoSave.getCantidad())
                .build();
    }

    @PutMapping("insumo")
    @ResponseStatus(HttpStatus.CREATED)
    public InsumoDto update(@RequestBody InsumoDto insumoDto){
        Insumo insumoUpdate = insumoService.save(insumoDto);

        return InsumoDto.builder()
                .id(insumoUpdate.getId())
                .nombre(insumoUpdate.getNombre())
                .cantidad(insumoUpdate.getCantidad())
                .build();
    }

    @DeleteMapping("insumo/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){ //recibe el parametro enviado por url
    //ResponseEntity maneja toda la respuesta HTTP incluyendo el cuerpo
    //cabecera y códigos de estado lo que permite libertad para configurar la respuesta enviada
        Map<String, Object> response = new HashMap<>();
        try{
            Insumo insumoDelete = insumoService.findById(id);
            insumoService.delete(insumoDelete);
            return new ResponseEntity<>(insumoDelete, HttpStatus.NO_CONTENT);
        }catch(DataAccessException exDt){
            response.put("mensaje", exDt.getMessage());
            response.put("insumo", null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("insumo/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InsumoDto showById(@PathVariable Integer id){
        Insumo insumo =  insumoService.findById(id);

        return InsumoDto.builder()
                .id(insumo.getId())
                .nombre(insumo.getNombre())
                .cantidad(insumo.getCantidad())
                .build();
    }
}
