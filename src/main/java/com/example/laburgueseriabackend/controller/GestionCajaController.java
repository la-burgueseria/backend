package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.GestionCajaDto;
import com.example.laburgueseriabackend.model.dto.ResumenGestionCajaDTO;
import com.example.laburgueseriabackend.model.entity.GestionCaja;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.IGestionCajaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:4200", "https://laburgueseria-ed758.web.app"})
public class GestionCajaController {
    @Autowired
    private IGestionCajaService gestionCajaService;

    //CREAR UN NUEVO REGISTRO DE CAJA
    @PostMapping("gestion-caja")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody GestionCajaDto gestionCajaDto){
        GestionCaja gestionCajaSave = null;

        try {

            //buscar si ya hay una caja abierta ese mismo dia
            // Establecer la hora a las 12:00 a.m.
            LocalDateTime fechaInicioConHora = LocalDateTime.of(LocalDate.from(gestionCajaDto.getFechaHorainicio()), LocalTime.MIN); // Establecer la hora a las 12:00 a.m.

            LocalDateTime fechaFinConHora = fechaInicioConHora.with(LocalTime.MAX);

            List<GestionCaja> gestionCajas = gestionCajaService.findGestionCajaByFechaHorainicio(fechaInicioConHora, fechaFinConHora);

            //Si no se encontraron registros que coincidan:
            if(gestionCajas.isEmpty()){
                gestionCajaSave = gestionCajaService.save(gestionCajaDto);

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Nuevo registro de caja creado correctamente")
                                .object(
                                        GestionCajaDto.builder()
                                                .id(gestionCajaSave.getId())
                                                .totalCalculado(gestionCajaSave.getTotalCalculado())
                                                .totalReportado(gestionCajaSave.getTotalReportado())
                                                .saldoInicioCajaMenor(gestionCajaSave.getSaldoInicioCajaMenor())
                                                .observaciones(gestionCajaSave.getObservaciones())
                                                .fechaHorainicio(gestionCajaSave.getFechaHorainicio())
                                                .fechaHoraCierre(gestionCajaSave.getFechaHoraCierre())
                                                .estadoCaja(gestionCajaSave.getEstadoCaja())
                                                .build()
                                )
                                .build()
                        , HttpStatus.CREATED
                );
            }else{ //En caso de que ya haya un registro de caja iniciado
              return new ResponseEntity<>(
                      MensajeResponse.builder()
                              .mensaje("Ya se ha iniciado o finalizado este dia laboral")
                              .object(null)
                              .build()
                      , HttpStatus.CONFLICT
              );
            }

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

    //LISTAR TODOS LOS REGISTROS DE CAJA
    @GetMapping("gestion-caja")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){
        try{
            List<GestionCaja> gestionCajas = gestionCajaService.listAll();

            if(gestionCajas.isEmpty()){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("No hay registros en el sistema")
                                .object(gestionCajas)
                                .build()
                        , HttpStatus.OK
                );
            }

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("OK")
                            .object(gestionCajas)
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

    //resumen de cierres de caja
    @GetMapping("gestion-caja/resumen")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getResumenGestionCaja(){
        List<ResumenGestionCajaDTO> resumenList = gestionCajaService.listAll().stream()
                .map(gestionCaja -> {
                    if(!gestionCaja.getEstadoCaja()){
                        return ResumenGestionCajaDTO.builder()
                                .fecha(gestionCaja.getFechaHorainicio().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                                .totalCalculado(gestionCaja.getTotalCalculado())
                                .totalReportado(gestionCaja.getTotalReportado())
                                .build();
                    }
                    return null; //devuelve null si estadoCaja es true
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("OK")
                        .object(resumenList)
                        .build()
                , HttpStatus.OK
        );
    }


    //obtener registros de caja dentro del horario establecido
    //dia actual desde las 12:00a.m. hasta el dia actual a las 11:59 p.m.
    @GetMapping("gestion-caja/fechas")
    public ResponseEntity<?> getGestionCajaByFecha(
            @RequestHeader(value = "fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestHeader(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin
    ){
        // Si la hora es menor a las 12 del medio dia, entonces le resta un dia y la asigna a las 12 del medio dia
        //en caso contrario simplemente asigna la hora de incio al medio dia
        LocalDateTime fechaInicioConHora = fechaInicio.minusDays(1).with(LocalTime.MIN);

        /*
         * si fechaFin no es nulo, entonces usa fechaFin.plusDays(1).minusSeconds(1),
         *  de lo contrario, usa fechaInicioConHora.plusDays(1).minusSeconds(1)
         * */
        LocalDateTime fechaFinConHora = (fechaFin != null) ? fechaFin.with(LocalTime.MAX) : fechaInicioConHora.with(LocalTime.MAX);

        try{
            List<GestionCaja> gestionCajas = gestionCajaService.findGestionCajaByFechaHorainicio(fechaInicioConHora, fechaFinConHora);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ok")
                            .object(gestionCajas)
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


    //ACTUALIZAR REGISTRO DE CAJA
    @PutMapping("gestion-caja/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody GestionCajaDto gestionCajaDto, @PathVariable Integer id){
        GestionCaja gestionCajaUpdate = null;

        try{
            if(gestionCajaService.existsById(id)){
                gestionCajaDto.setId(id);
                gestionCajaUpdate = gestionCajaService.save(gestionCajaDto);

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Registro de caja actualizado exitosamente")
                                .object(
                                        GestionCajaDto.builder()
                                                .id(gestionCajaUpdate.getId())
                                                .totalCalculado(gestionCajaUpdate.getTotalCalculado())
                                                .totalReportado(gestionCajaUpdate.getTotalReportado())
                                                .saldoInicioCajaMenor(gestionCajaUpdate.getSaldoInicioCajaMenor())
                                                .observaciones(gestionCajaUpdate.getObservaciones())
                                                .fechaHorainicio(gestionCajaUpdate.getFechaHorainicio())
                                                .fechaHoraCierre(gestionCajaUpdate.getFechaHoraCierre())
                                                .estadoCaja(gestionCajaUpdate.getEstadoCaja())
                                                .build()
                                )
                                .build()
                        , HttpStatus.CREATED
                );
            }
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Registro no encontrado en la base de datos")
                    .object(null)
                    .build()
                    , HttpStatus.NOT_FOUND
            );
        }catch(DataAccessException exDt){
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje(exDt.getMessage())
                    .object(null)
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    //eliminar gestion caja
    @DeleteMapping("gestion-caja/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        try{
            GestionCaja gestionCajaDelete = gestionCajaService.findById(id);
            if(gestionCajaDelete != null){
                gestionCajaService.delete(gestionCajaDelete);
                return new ResponseEntity<>(gestionCajaDelete, HttpStatus.NO_CONTENT);
            }
            else{
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("No se ha encontrado el elemento a eliminar")
                                .object(null)
                                .build()
                        , HttpStatus.NOT_FOUND
                );
            }
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