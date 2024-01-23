package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.CuentaDto;
import com.example.laburgueseriabackend.model.entity.Cuenta;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.ICuentaService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CuentaController {
    @Autowired
    private ICuentaService cuentaService;

    //LISTAR TODAS LAS CUENTAS
    @GetMapping("cuentas")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){
        try{
            List<Cuenta> cuentas = cuentaService.listAll();

            if(cuentas.isEmpty()){
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
                            .object(cuentas)
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

    //  CREAR CUENTA
    @PostMapping( "cuenta")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody CuentaDto cuentaDto){
        Cuenta cuentaSave = null;
        try{
            cuentaSave = cuentaService.save(cuentaDto);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Cuenta creada exitosamente")
                            .object(
                                    Cuenta.builder()
                                            .id(cuentaSave.getId())
                                            .fecha(cuentaSave.getFecha())
                                            .estadoCuenta(cuentaDto.getEstadoCuenta())
                                            .mesa(cuentaDto.getMesa())
                                            .cuentaProductos(cuentaDto.getCuentaProductos())
                                            .total(cuentaSave.getTotal())
                                            .abono(cuentaSave.getAbono())
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

    //buscar por id
    @GetMapping("cuenta/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<?> showById(@PathVariable Integer id){
        try{
            Cuenta cuenta = cuentaService.findById(id);

            if(cuenta != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("OK")
                                .object(
                                        CuentaDto.builder()
                                                .id(cuenta.getId())
                                                .estadoCuenta(cuenta.getEstadoCuenta())
                                                .cuentaProductos(cuenta.getCuentaProductos())
                                                .mesa(cuenta.getMesa())
                                                .fecha(cuenta.getFecha())
                                                .total(cuenta.getTotal())
                                                .abono(cuenta.getAbono())
                                                .build()
                                )
                                .build()
                        , HttpStatus.FOUND
                );
            }
            return new ResponseEntity<>(MensajeResponse
                    .builder()
                    .mensaje("Registro no encontrado")
                    .object(null)
                    .build()
                    , HttpStatus.CONFLICT);
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
    //obtener cuentas dentro del horario establecido
    //dia actual desde las 12:00p.m. hasta el dia siguiente a las 11:59 a.m.
    @GetMapping("cuenta/fechas")
    public ResponseEntity<?> getcuentasByFecha(
            @RequestHeader(value = "fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestHeader(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin)
    {
        // Establecer la hora a las 12:00 p.m.
        LocalDateTime fechaInicioConHora = LocalDateTime.of(LocalDate.from(fechaInicio), LocalTime.NOON); // Establecer la hora a las 12:00 p.m.

        /*
        * si fechaFin no es nulo, entonces usa fechaFin.plusDays(1).minusSeconds(1),
        *  de lo contrario, usa fechaInicioConHora.plusDays(1).minusSeconds(1)
        * */
        LocalDateTime fechaFinConHora = (fechaFin != null) ? fechaFin.plusDays(1).minusSeconds(1) : fechaInicioConHora.plusDays(1).minusSeconds(1);

        try{
            List<Cuenta> cuentas = cuentaService.getcuentasByFecha(fechaInicioConHora, fechaFinConHora);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ok")
                            .object(cuentas)
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

    //eliminar cuenta
    @DeleteMapping("cuenta/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        try{
            Cuenta cuentaDelete = cuentaService.findById(id);
            cuentaService.delete(cuentaDelete);
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("cuenta eliminada correctamente")
                            .object(cuentaDelete)
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
    //ACTUALIZAR CUENTA
    @PutMapping("cuenta/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody CuentaDto cuentaDto, @PathVariable Integer id){
        Cuenta cuentaUpdate = null;

        try{
            if(cuentaService.existsById(id)){
                cuentaDto.setId(id);
                cuentaUpdate = cuentaService.save(cuentaDto);

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Cuenta actualizada correctamente")
                                .object(
                                        CuentaDto.builder()
                                                .id(cuentaUpdate.getId())
                                                .estadoCuenta(cuentaUpdate.getEstadoCuenta())
                                                .cuentaProductos(cuentaUpdate.getCuentaProductos())
                                                .mesa(cuentaUpdate.getMesa())
                                                .fecha(cuentaUpdate.getFecha())
                                                .total(cuentaUpdate.getTotal())
                                                .abono(cuentaUpdate.getAbono())
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
    //CUENTAS PAGINADAS
    @GetMapping("cuentas-page")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<Cuenta>> cuentasPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String order,
            @RequestParam(defaultValue = "true") boolean asc
    ){
        Page<Cuenta> cuentas = cuentaService.cuentasPaginadas(
                PageRequest.of(page, size, Sort.by(order))
        );

        if(!asc){
            cuentas = cuentaService.cuentasPaginadas(
                    PageRequest.of(page, size, Sort.by(order).descending())
            );
        }
        return new ResponseEntity<Page<Cuenta>>(cuentas, HttpStatus.OK);
    }
}
