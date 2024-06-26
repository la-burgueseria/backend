package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.InsumoDto;
import com.example.laburgueseriabackend.model.entity.Insumo;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.IInsumoService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController //especificar que esta clase es un controller
@RequestMapping("/api/v1") //asignacion de la ruta para ser consumido
@CrossOrigin(origins = "*")
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
    //PAGINACION DE INSUMOS
    @GetMapping("insumos2")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<Insumo>> insumosPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String order,
            @RequestParam(defaultValue = "true") boolean asc
    ){
        Page<Insumo> insumos = insumoService.insumosPaginados(
                PageRequest.of(page,size, Sort.by(order)));
                if(!asc){
                    insumos = insumoService.insumosPaginados(
                            PageRequest.of(page,size, Sort.by(order).ascending()));
                }
        return  new ResponseEntity<Page<Insumo>>(insumos, HttpStatus.OK);
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
                insumoExists.setPrecioCompraUnidad(insumoDto.getPrecioCompraUnidad());
                Integer id1 = insumoDto.getId();
                Integer id2 = insumoExists.getId();


                insumoDto =  InsumoDto.builder()
                        .id(insumoExists.getId())
                        .nombre(insumoExists.getNombre())
                        .cantidad(insumoExists.getCantidad())
                        .precioCompraUnidad(insumoExists.getPrecioCompraUnidad())
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
                        .precioCompraUnidad(insumoSave.getPrecioCompraUnidad())
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
    public ResponseEntity<?> update(@RequestBody InsumoDto insumoDto, @PathVariable Integer id) {
        try {
            // Verificar si el insumo con el ID proporcionado existe
            if (insumoService.existsById(id)) {
                // Establecer el ID del DTO con el ID del path
                insumoDto.setId(id);

                // Buscar un insumo por su nombre
                Insumo insumobyNombre = this.insumoService.findByNombre(insumoDto.getNombre());

                // Verificar si se encontró un insumo con el mismo nombre y diferente ID
                if (insumobyNombre != null && !Objects.equals(insumoDto.getId(), insumobyNombre.getId())) {
                    // Si el insumo encontrado tiene un ID diferente, devolver un mensaje de conflicto
                    return new ResponseEntity<>(
                            MensajeResponse.builder()
                                    .mensaje("Ya existe un insumo con este nombre")
                                    .object(null)
                                    .build(),
                            HttpStatus.CONFLICT
                    );
                }

                // Guardar el insumo actualizado
                Insumo insumoUpdate = insumoService.save(insumoDto);

                // Devolver una respuesta exitosa con el insumo actualizado
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Actualizado correctamente")
                                .object(InsumoDto.builder()
                                        .id(insumoUpdate.getId())
                                        .nombre(insumoUpdate.getNombre())
                                        .cantidad(insumoUpdate.getCantidad())
                                        .build())
                                .build(),
                        HttpStatus.CREATED
                );
            } else {
                // Si el insumo con el ID proporcionado no existe, devolver un mensaje de no encontrado
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Registro no encontrado en la base de datos")
                                .object(null)
                                .build(),
                        HttpStatus.NOT_FOUND
                );
            }
        } catch (DataAccessException exDt) {
            // Si ocurre una excepción al acceder a los datos, devolver un mensaje de error interno del servidor
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR
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
                .precioCompraUnidad(insumo.getPrecioCompraUnidad())
                .build();

        return new ResponseEntity<>(MensajeResponse
                .builder()
                .mensaje("OK")
                .object(insumoDto)
                .build()
                , HttpStatus.OK
        );
    }
    //buscar insumo por nombre
    @GetMapping("insumo/buscar/{nombre}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> buscarPorNombre(@PathVariable String nombre){
        List<Insumo> insumos;

        try{
            insumos  = insumoService.findInsumoByNameContaining(nombre.toUpperCase());
            if(insumos.isEmpty()){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("OK")
                                .object(insumos)
                                .build()
                        , HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("ok")
                            .object(insumos)
                            .build()
                    , HttpStatus.OK
            );
        }catch(DataAccessException exDt){

            return new ResponseEntity<>(MensajeResponse
                    .builder()
                    .mensaje(exDt.getMessage())
                    .object(null)
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
