package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.EgresoDto;
import com.example.laburgueseriabackend.model.entity.Egreso;
import com.example.laburgueseriabackend.model.entity.Ingreso;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.IEgresoService;
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
public class EgresoController {
    @Autowired
    private IEgresoService egresoService;

    //crear egreso
    @PostMapping("egreso")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody EgresoDto egresoDto){
        Egreso egresoSave, egresoExists = null;

        try{
            egresoExists = egresoService.findById(egresoDto.getId());

            if(egresoExists != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ya existe un ingreso")
                                .object(null)
                                .build()
                        , HttpStatus.CONFLICT
                );
            }

            egresoSave = egresoService.save(egresoDto);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ingreso creado exitosamente")
                            .object(egresoSave)
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

    //Listar todos los egresos
    @GetMapping("egresos")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){
        try{
            List<Egreso> egresos = egresoService.listAll();

            if(egresos.isEmpty()){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("No hay registros para ingresos")
                                .object(null)
                                .build()
                        , HttpStatus.NOT_FOUND
                );
            }

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("OK")
                            .object(egresos)
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

    //paginacion egresos
    @GetMapping("egresos-page")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> egresosPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String order,
            @RequestParam(defaultValue = "true") boolean asc
    ){
        Page<Egreso> egresos  = egresoService.egresosPaginados(
                PageRequest.of(page, size, Sort.by(order))
        );

        if(!asc){
            egresos = egresoService.egresosPaginados(
                    PageRequest.of(page, size, Sort.by(order).descending())
            );
        }

        return new ResponseEntity<Page<Egreso>>(egresos, HttpStatus.OK);
    }

    //egresos paginados y filtrados por fechas
    @GetMapping("egresos-page/fecha")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> egresosPageFecha(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String order,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestHeader(value = "fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestHeader(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin
    ){
        // Establecer la hora a las 12:00 p.m.
        LocalDateTime fechaInicioConHora = LocalDateTime.of(LocalDate.from(fechaInicio), LocalTime.NOON); // Establecer la hora a las 12:00 p.m.

        /*
         * si fechaFin no es nulo, entonces usa fechaFin.plusDays(1).minusSeconds(1),
         *  de lo contrario, usa fechaInicioConHora.plusDays(1).minusSeconds(1)
         * */
        LocalDateTime fechaFinConHora = (fechaFin != null) ? fechaFin.plusDays(1).minusSeconds(1) : fechaInicioConHora.plusDays(1).minusSeconds(1);

        try{
            Page<Egreso> egresos = egresoService.findByFechaBetween(fechaInicioConHora, fechaFinConHora, PageRequest.of(page, size, Sort.by(order)) );

            if(!asc){
                egresos = egresoService.findByFechaBetween(fechaInicioConHora, fechaFinConHora, PageRequest.of(page, size, Sort.by(order).descending()) );
            }

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ok")
                            .object(egresos)
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
