package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.EstadoMesaDto;
import com.example.laburgueseriabackend.model.entity.EstadoMesa;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.IEstadoMesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EstadoMesaController {
    @Autowired
    private IEstadoMesaService estadoMesaService;

    //LISTAR TODOS LOS ESTADOS DE LAS MESAS
    @GetMapping("estados-mesas")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){
        try{
            List<EstadoMesa> estadosMesas = estadoMesaService.listAll();
            if(estadosMesas.isEmpty()){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Aún no existe ningún estado vinculado a una mesa")
                                .object(null)
                                .build()
                        , HttpStatus.OK
                );
            }

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ok")
                            .object(estadosMesas)
                            .build()
                    , HttpStatus.OK
            );
        }catch(DataAccessException exDt){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    //CREAR ESTADO MESA
    @PostMapping("estado-mesa")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody EstadoMesaDto estadoMesaDto){
        EstadoMesa estadoMesaSave, estadoMesaExists = null;

        try{
            estadoMesaExists = estadoMesaService.findByNombre(estadoMesaDto.getNombre());

            if(estadoMesaExists == null){
                estadoMesaSave = estadoMesaService.save(estadoMesaDto);

                estadoMesaDto = EstadoMesaDto.builder()
                        .id(estadoMesaSave.getId())
                        .nombre(estadoMesaSave.getNombre())
                        .build();

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Estado de mesa creado exitosamente")
                                .object(estadoMesaDto)
                                .build()
                        , HttpStatus.CREATED
                );
            }
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ya existe un estado para esta categoría")
                            .object(null)
                            .build()
                    , HttpStatus.CONFLICT
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

    //ACTUALIZAR ESTADO MESA
    @PutMapping("estado-mesa/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody EstadoMesaDto estadoMesaDto, @PathVariable Integer id){
        EstadoMesa estadoMesaUpdate, estadoMesaExists = null;

        try{
            estadoMesaExists = estadoMesaService.findByNombre(estadoMesaDto.getNombre());

            if(estadoMesaExists == null && estadoMesaService.existsById(id)){
                estadoMesaDto.setId(id);

                estadoMesaUpdate = estadoMesaService.save(estadoMesaDto);

                estadoMesaDto = EstadoMesaDto.builder()
                        .id(estadoMesaUpdate.getId())
                        .nombre(estadoMesaUpdate.getNombre())
                        .build();

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Estado de mesa actualizado exitosamente")
                                .object(estadoMesaDto)
                                .build()
                        , HttpStatus.CREATED
                );
            }
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ya existe un estado para esta categoría")
                            .object(null)
                            .build()
                    , HttpStatus.CONFLICT
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

    //ELIMINAR ESTADO MESA
    @DeleteMapping("estado-mesa/({id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        try{
            EstadoMesa estadoMesaDelete = estadoMesaService.findById(id);
            estadoMesaService.delete(estadoMesaDelete);
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Estado de mesa eliminado correctamente")
                            .object(null)
                            .build()
                    , HttpStatus.NO_CONTENT
            );
        }catch (DataAccessException exDt){
            return new ResponseEntity<>(MensajeResponse
                    .builder()
                    .mensaje(exDt.getMessage())
                    .object(null)
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //BUSCAR POR ID
    @GetMapping("estado-mesa/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showById(@PathVariable Integer id){
        EstadoMesa estadoMesa = null;
        EstadoMesaDto estadoMesaDto = null;

        try{
            estadoMesa = estadoMesaService.findById(id);

            if(estadoMesa == null){
                return new ResponseEntity<>(MensajeResponse
                        .builder()
                        .mensaje("Registro no encontrado")
                        .object(null)
                        .build()
                        , HttpStatus.INTERNAL_SERVER_ERROR);
            }

            estadoMesaDto = EstadoMesaDto.builder()
                    .id(estadoMesa.getId())
                    .nombre(estadoMesa.getNombre())
                    .mesas(estadoMesa.getMesas())
                    .build();

            return new ResponseEntity<>(MensajeResponse
                    .builder()
                    .mensaje("OK")
                    .object(estadoMesaDto)
                    .build()
                    , HttpStatus.OK
            );
        }catch (DataAccessException exDt){
            return new ResponseEntity<>(MensajeResponse
                    .builder()
                    .mensaje("Registro no encontrado")
                    .object(null)
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
