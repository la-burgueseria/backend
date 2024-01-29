package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.GestionCajaDto;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GestionCajaController {
    @Autowired
    private IGestionCajaService gestionCajaService;

    //CREAR UN NUEVO REGISTRO DE CAJA
    @PostMapping("gestion-caja")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody GestionCajaDto gestionCajaDto){
        GestionCaja gestionCajaSave = null;

        try {
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
                                .object(null)
                                .build()
                        , HttpStatus.NOT_FOUND
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

    //obtener registros de caja dentro del horario establecido
    //dia actual desde las 12:00p.m. hasta el dia siguiente a las 11:59 a.m.
    @GetMapping("gestion-caja/fechas")
    public ResponseEntity<?> getGestionCajaByFecha(
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
}