package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.EgresoDto;
import com.example.laburgueseriabackend.model.dto.ResumenEgresoDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class EgresoController {
    @Autowired
    private IEgresoService egresoService;

    //crear egreso
    @PostMapping("egreso")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(
            @RequestBody(required = false) byte[] soporte,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("fecha") String fecha,
            @RequestParam("total") Double total,
            @RequestParam("categoria") String categoria,
            @RequestParam("deduccionDesde") String deduccionDesde

    ){
        Egreso egresoSave, egresoExists = null;
        EgresoDto egresoDto = null;

        try{

            if(soporte == null){
                egresoDto = EgresoDto.builder()
                        .id(0)
                        .descripcion(descripcion)
                        .fecha(null)
                        .total(total)
                        .categoria(categoria)
                        .deduccionDesde(deduccionDesde)
                        .soporte(null)
                        .build();
            }else{
                egresoDto = EgresoDto.builder()
                        .id(0)
                        .descripcion(descripcion)
                        .fecha(null)
                        .total(total)
                        .categoria(categoria)
                        .deduccionDesde(deduccionDesde)
                        .soporte(soporte)
                        .build();
            }

            egresoSave = egresoService.save(egresoDto, soporte);

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
    //resumen de egresos
    @GetMapping("egresos/resumen")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getResumenEgresos(){
        List<Egreso> egresos = egresoService.listAll();

        // Agrupar por fecha y sumar los valores
        Map<String, Double> resumen = egresos.stream()
                .collect(Collectors.groupingBy(
                        egreso -> egreso.getFecha().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        Collectors.summingDouble(Egreso::getTotal)
                ));

        List<ResumenEgresoDTO> resumenDTOs = resumen.entrySet().stream()
                .map(entry -> new ResumenEgresoDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("OK")
                        .object(resumenDTOs)
                        .build()
                , HttpStatus.OK
        );
    }

    //paginacion egresos
    @GetMapping("egresos/fechas-horario-laboral")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> egresosPage(
            @RequestHeader(value = "fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestHeader(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin
    ){

        // Establecer la hora a las 12:00 a.m.
        LocalDateTime fechaInicioConHora = LocalDateTime.of(LocalDate.from(fechaInicio), LocalTime.MIN); // Establecer la hora a las 12:00 p.m.

        // Establecer la hora a las 23:59:59 para fechaFin, o usar la fechaInicioConHora si fechaFin es nulo
        LocalDateTime fechaFinConHora = (fechaFin != null) ? fechaFin.with(LocalTime.MAX) : fechaInicioConHora.with(LocalTime.MAX);

        try{
           List<Egreso> egresos = egresoService.findEgresoByFechas(fechaInicioConHora, fechaFinConHora);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ok")
                            .object(egresos)
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
        // Establecer la hora a las 12:00 a.m.
        LocalDateTime fechaInicioConHora = LocalDateTime.of(LocalDate.from(fechaInicio), LocalTime.MIN); // Establecer la hora a las 12:00 p.m.

        // Establecer la hora a las 23:59:59 para fechaFin, o usar la fechaInicioConHora si fechaFin es nulo
        LocalDateTime fechaFinConHora = (fechaFin != null) ? fechaFin.with(LocalTime.MAX) : fechaInicioConHora.with(LocalTime.MAX);

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

    //ELIMINAR EGRESO
    @DeleteMapping("egreso/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        try{
            Egreso egresoDelete = egresoService.findById(id);
            egresoService.delete(egresoDelete);
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("El egreso ha sido eliminado correctamente.")
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
}
