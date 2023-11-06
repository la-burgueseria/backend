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
                                            .total(cuentaSave.getTotal())
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
}
