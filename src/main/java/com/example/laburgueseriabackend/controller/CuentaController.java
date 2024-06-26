package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.CuentaDto;
import com.example.laburgueseriabackend.model.entity.Cuenta;
import com.example.laburgueseriabackend.model.entity.Empleado;
import com.example.laburgueseriabackend.model.entity.EstadoCuenta;
import com.example.laburgueseriabackend.model.entity.Mesa;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.ICuentaService;
import com.example.laburgueseriabackend.service.IEmpleadoService;
import com.example.laburgueseriabackend.service.IMesaService;
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
@CrossOrigin(origins = {"http://localhost:4200", "https://laburgueseria-ed758.web.app"})
public class CuentaController {
    @Autowired
    private ICuentaService cuentaService;
    @Autowired
    private IEmpleadoService empleadoService;
    @Autowired
    private IMesaService mesaService;

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
        Mesa mesa = null;
        try{
            mesa = mesaService.findById(cuentaDto.getMesa().getId());
            if(mesa.getIsOcupada()){
                Cuenta cuentaExistente = cuentaService.getCuentasActivasByNumeroMesa(mesa.getNumeroMesa());
                cuentaDto.setTotal(cuentaDto.getTotal() + cuentaExistente.getTotal());
                cuentaDto.setId(cuentaExistente.getId());
                cuentaDto.setFecha(cuentaExistente.getFecha());
                cuentaSave = cuentaService.save(cuentaDto
                );
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ya existe una cuenta para esta mesa")
                                .object(cuentaSave)
                                .build()
                        , HttpStatus.OK
                );
            }else{
                cuentaSave = cuentaService.save(cuentaDto);
                //cambiar la mesa a ocupada
                mesaService.changeOcupacionMesa(mesa.getId(), true);
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
        // Si la hora es menor a las 12 del medio dia, entonces le resta un dia y la asigna a las 12 del medio dia
        //en caso contrario simplemente asigna la hora de incio al medio dia

        LocalDateTime fechaInicioConHora = fechaInicio.with(LocalTime.MIN);


        /*
        * si fechaFin no es nulo, entonces usa fechaFin.plusDays(1).minusSeconds(1),
        *  de lo contrario, usa fechaInicioConHora.plusDays(1).minusSeconds(1)
        * */
        LocalDateTime fechaFinConHora = (fechaFin != null) ? fechaFin.with(LocalTime.MAX) : fechaInicioConHora.with(LocalTime.MAX);

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
    //filtrar cuentas por empleado y dos fechas dadas.
    @GetMapping("cuenta/empleado/fechas")
     public ResponseEntity<?> getCuentasEmpleadoByFechas(
            @RequestHeader(value = "fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestHeader(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestHeader(value="empleadoId") Integer empleadoId
    )   {
        Empleado empleado = null;


        //se asigna la hora al inicio del dia
        LocalDateTime fechaInicioConHora = fechaInicio.withHour(0).withMinute(0).withSecond(0).withNano(0);
        //se asigna la hora al final del dia
        LocalDateTime fechaFinConHora = (fechaFin != null) ? fechaFin.withHour(23).withMinute(59).withSecond(59).withNano(999999999): fechaInicioConHora.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        try{
            empleado = this.empleadoService.findById(empleadoId);

            if(empleado == null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("No se ha encontrado un empleado con este id")
                                .object(null)
                                .build()
                        , HttpStatus.NOT_FOUND
                );
            }
            List<Cuenta> cuentas = this.cuentaService.getCuentasByEmpleado(empleado.getId(), fechaInicioConHora, fechaFinConHora);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("OK")
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
