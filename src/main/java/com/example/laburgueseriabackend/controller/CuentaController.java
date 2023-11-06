package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.CuentaDto;
import com.example.laburgueseriabackend.model.entity.Cuenta;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.ICuentaService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
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
                                            .id(cuentaDto.getId())
                                            .fecha(cuentaSave.getFecha())
                                            .estadoCuenta(cuentaDto.getEstadoCuenta())
                                            .mesa(cuentaDto.getMesa())
                                            .cuentaProductos(cuentaDto.getCuentaProductos())
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
