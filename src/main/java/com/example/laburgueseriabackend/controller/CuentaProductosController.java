package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.CuentaProductosDto;
import com.example.laburgueseriabackend.model.entity.CuentaProductos;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.ICuentaProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CuentaProductosController {
    @Autowired
    private ICuentaProductosService cuentaProductosService;

    // CREAR
    @PostMapping("cuenta-productos")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> save(@RequestBody CuentaProductosDto cuentaProductosDto){
        CuentaProductos cuentaProductosSave = null;

        try{
            cuentaProductosSave = cuentaProductosService.save(cuentaProductosDto);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Creado exitosamente")
                            .object(
                                    CuentaProductos.builder()
                                            .id(cuentaProductosSave.getId())
                                            .cantidad(cuentaProductosSave.getCantidad())
                                            .cuenta(cuentaProductosSave.getCuenta())

                                            .build()
                            )
                            .build()
                    , HttpStatus.CREATED
            );
        }catch(DataAccessException exDt){
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
