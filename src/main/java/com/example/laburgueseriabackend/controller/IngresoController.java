package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.IngresoDto;
import com.example.laburgueseriabackend.model.entity.Ingreso;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.IIngresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:4200", "https://laburgueseria-ed758.web.app"})
public class IngresoController {
    @Autowired
    private IIngresoService ingresoService;

    //CREAR INGREDO
    @PostMapping("ingreso")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody IngresoDto ingresoDto){
        Ingreso ingresoSave, ingresoExists = null;

        try{
            ingresoExists = ingresoService.findById(ingresoDto.getId());

            if(ingresoExists != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ya existe un ingreso")
                                .object(null)
                                .build()
                        , HttpStatus.CONFLICT
                );
            }

            ingresoSave = ingresoService.save(ingresoDto);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ingreso creado exitosamente")
                            .object(ingresoSave)
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

    //Listar todos los ingresos
    @GetMapping("ingresos")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){
        try{
            List<Ingreso> ingresos = ingresoService.listAll();

            if(ingresos.isEmpty()){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("No hay registros para ingresos")
                                .object(null)
                                .build()
                        , HttpStatus.OK
                );
            }

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("OK")
                            .object(ingresos)
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

    //PAGINACION INGRESOS
    @GetMapping("ingresos-page")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<Ingreso>> ingresosPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String order,
            @RequestParam(defaultValue = "true") boolean asc
    ){
        Page<Ingreso> ingresos  = ingresoService.ingresosPaginados(
                PageRequest.of(page, size, Sort.by(order))
        );

        if(!asc){
            ingresos = ingresoService.ingresosPaginados(
                    PageRequest.of(page, size, Sort.by(order).descending())
            );
        }

        return new ResponseEntity<Page<Ingreso>>(ingresos, HttpStatus.OK);
    }

    //BUSCAR INGRESO POR ID
    @GetMapping("ingreso/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showById(@PathVariable Integer id){
        try{
            Ingreso ingreso = ingresoService.findById(id);

            if(ingreso != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("OK")
                                .object(ingreso)
                                .build()
                        , HttpStatus.OK
                );
            }
            return new ResponseEntity<>(MensajeResponse
                    .builder()
                    .mensaje("Registro no encontrado")
                    .object(null)
                    .build()
                    , HttpStatus.OK);
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

    //actualizar ingreso
    //EN caso de que lo necesite mas adelante
    // la idea es que no se puedan eliminar ni editar
    // los ingresos por temas contables.
    @PutMapping("ingreso/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> Update(@RequestBody IngresoDto ingresoDto, @PathVariable Integer id){
        Ingreso ingresoUpdate = null;

        try{
            if(ingresoService.existsById(id)){
                ingresoDto.setId(id);

                ingresoUpdate = ingresoService.save(ingresoDto);

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El ingreso ha sido actualizado correctamente.")
                                .object(ingresoUpdate)
                                .build()
                        , HttpStatus.CREATED
                );
            }
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No se ha encontrado el ingreso.")
                            .object(null)
                            .build()
                    , HttpStatus.CREATED
            );
        }catch (DataAccessException exDt){
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje(exDt.getMessage())
                    .object(null)
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


    //BUSCAR INGRESOS QUE ESTEN ENTRE DOS FECHAS DADAS
    @GetMapping("ingreso/fechas/{fechaInicio}/{fechaFin}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> ingresoFecha(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin
    ){
        try{
            //se envia unicamente la fecha por variable
            //se usa atStartofDay para indicarle que se debe empezar a contar desde las 00:00:01
            // y para indicar fin del dia añadimos manualmente las horas en este caso 23, 59, 59 (HORA MILITAR)
            List<Ingreso> ingresos = ingresoService.findIngresoByFechas(fechaInicio.atStartOfDay(), fechaFin.atTime(23,59,59));

            if(ingresos.isEmpty()){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("No hay registros para ingresos")
                                .object(null)
                                .build()
                        , HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("OK")
                            .object(ingresos)
                            .build()
                    , HttpStatus.OK
            );
        }catch (DataAccessException exDt){
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje(exDt.getMessage())
                    .object(null)
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    //filtrar ingreso por fechas y devolver paginacion
    @GetMapping("ingreso/fecha-page")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> ingresoPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String order,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestHeader(value = "fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestHeader(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin
    ){
        // Establecer la hora a las 12:00 a.m.
        LocalDateTime fechaInicioConHora = LocalDateTime.of(LocalDate.from(fechaInicio), LocalTime.MIN); // Establecer la hora a las 12:00 p.m.

        // Establecer la hora a las 23:59:59 para fechaFin, o usar la fechaInicioConHora si fechaFin es nulo
        LocalDateTime fechaFinConHora = (fechaFin != null) ? fechaFin.with(LocalTime.MAX) : fechaInicioConHora.with(LocalTime.MAX);

        try{
            Page<Ingreso> ingresos = ingresoService.findByFechaBetween(fechaInicioConHora, fechaFinConHora, PageRequest.of(page, size, Sort.by(order)) );

            if(!asc){
                ingresos = ingresoService.findByFechaBetween(fechaInicioConHora, fechaFinConHora, PageRequest.of(page, size, Sort.by(order).descending()) );
            }

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ok")
                            .object(ingresos)
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
}