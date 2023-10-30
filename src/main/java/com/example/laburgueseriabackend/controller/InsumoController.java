package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.entity.Insumo;
import com.example.laburgueseriabackend.service.IInsumo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController //especificar que esta clase es un controller
@RequestMapping("/api/v1") //asignacion de la ruta para ser consumido
public class InsumoController {

    @Autowired //inyeccion de dependencias del servicio
    private IInsumo insumoService;

    @PostMapping("cliente") //rutaa para lanzar el evento post
    public Insumo create(@RequestBody Insumo insumo){ //request body transforma el json enviado al objeto necesitado
        return insumoService.save(insumo);
    }

    @PutMapping("cliente")
    public Insumo update(@RequestBody Insumo insumo){
        return insumoService.save(insumo);
    }

    @DeleteMapping("cliente/{id}")
    public void delete(@PathVariable Integer id){ //recibe el parametro enviado por url
        Insumo insumoDelete = insumoService.findById(id);
        insumoService.delete(insumoDelete);
    }

    @GetMapping("cliente/{id}")
    public Insumo showById(@PathVariable Integer id){
        return insumoService.findById(id);
    }
}
