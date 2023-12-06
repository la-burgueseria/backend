package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.IngresoCuentaDto;
import com.example.laburgueseriabackend.model.entity.IngresoCuenta;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.ICuentaService;
import com.example.laburgueseriabackend.service.IIngresoCuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class IngresoCuentaController {
    @Autowired
    private IIngresoCuentaService ingresoCuentaService;
    @Autowired
    private ICuentaService cuentaService;

    @PostMapping("ingreso-cuenta")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody IngresoCuentaDto ingresoCuentaDto){
        IngresoCuenta ingresoCuentaSave = null;

        try{
            ingresoCuentaSave = ingresoCuentaService.save(ingresoCuentaDto);

            /*
            * TODO: hacer que cambie el estado de la cuenta a PAGADA
            *  al registrarla en ingreso X cuenta
            * */

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Venta completada exitosamente!")
                            .object(
                                    IngresoCuentaDto.builder()
                                            .id(ingresoCuentaSave.getId())
                                            .cuenta(ingresoCuentaSave.getCuenta())
                                            .ingreso(ingresoCuentaSave.getIngreso())
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

    // listar todas las cuentas por ingreso
    @GetMapping("ingreso-cuentas")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){
        try{
            List<IngresoCuenta> ingresoCuentas = ingresoCuentaService.listAll();

            if(ingresoCuentas.isEmpty()){
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
                            .mensaje("Ok")
                            .object(ingresoCuentas)
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

    //Buscar por id
    @GetMapping("ingreso-cuenta/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showById(@PathVariable Integer id){
        try{
            IngresoCuenta ingresoCuenta = ingresoCuentaService.findById(id);

            if(ingresoCuenta != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ok, encontrado")
                                .object(
                                        IngresoCuentaDto.builder()
                                                .id(ingresoCuenta.getId())
                                                .ingreso(ingresoCuenta.getIngreso())
                                                .cuenta(ingresoCuenta.getCuenta())
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
                    , HttpStatus.NOT_FOUND);
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

    //OBTENER LAS CUENTAS DE UN INGRESO ESPECIFICO
    @GetMapping("ingreso-cuenta/ingreso/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> findByIngreso(@PathVariable Integer id){
        try{
            List<IngresoCuenta> ingresoCuentas = ingresoCuentaService.obtenerCuentasPorIngreso(id);

            if(!ingresoCuentas.isEmpty()){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ok, encontrado")
                                .object(ingresoCuentas)
                                .build()
                        , HttpStatus.FOUND
                );
            }
            return new ResponseEntity<>(MensajeResponse
                    .builder()
                    .mensaje("Registro no encontrado")
                    .object(null)
                    .build()
                    , HttpStatus.NOT_FOUND);
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
