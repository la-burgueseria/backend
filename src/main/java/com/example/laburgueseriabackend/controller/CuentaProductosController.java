package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.CuentaProductosDto;
import com.example.laburgueseriabackend.model.entity.Cuenta;
import com.example.laburgueseriabackend.model.entity.CuentaProductos;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.ICuentaProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:4200", "https://laburgueseria-ed758.web.app"})
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
                                            .valorProducto(cuentaProductosSave.getValorProducto())
                                            .cuenta(cuentaProductosSave.getCuenta())
                                            .producto(cuentaProductosSave.getProducto())
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
    //LISTAR
    @GetMapping("cuentas-productos")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){
        try{
            List<CuentaProductos> cuentasProductos = cuentaProductosService.listAll();

            if(cuentasProductos.isEmpty()){
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
                            .object(cuentasProductos)
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
    @GetMapping("cuenta-productos/cuenta/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showByCuentaId(@PathVariable Integer id){
        try{
            List<CuentaProductos> cuentaProductos = cuentaProductosService.getCuentaProductosByCuenta(id);
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ok")
                            .object(cuentaProductos)
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
    //BUSCAR POR ID
    @GetMapping("cuenta-productos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showById(@PathVariable Integer id){
        try{
            CuentaProductos cuentaProductos = cuentaProductosService.findById(id);

            if(cuentaProductos != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ok, encontrado")
                                .object(
                                        CuentaProductosDto.builder()
                                                .id(cuentaProductos.getId())
                                                .producto(cuentaProductos.getProducto())
                                                .cantidad(cuentaProductos.getCantidad())
                                                .valorProducto(cuentaProductos.getValorProducto())
                                                .estado(cuentaProductos.getEstado())
                                                .cuenta(cuentaProductos.getCuenta())
                                                .build()
                                )
                                .build()
                        ,HttpStatus.FOUND
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

    //ELIMINAR REGISTRO
    @DeleteMapping("cuenta-productos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        try{
            CuentaProductos cuentaProductos = cuentaProductosService.findById(id);

            cuentaProductosService.deleteCuentaProducto(cuentaProductos.getId());

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Producto eliminado correctamente")
                            .object(cuentaProductos)
                            .build()
                    , HttpStatus.OK
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

    //ACTUALIZAR REGISTRO
    @PutMapping("cuenta-productos/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody CuentaProductosDto cuentaProductosDto, @PathVariable Integer id){
        CuentaProductos cuentaProductosExists, cuentaProductosUpdate = null;

        try{
            cuentaProductosExists = cuentaProductosService.findById(id);
            if(cuentaProductosExists != null){
                cuentaProductosDto.setId(id);

                cuentaProductosUpdate = cuentaProductosService.save(cuentaProductosDto);

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Registro actualizado correctamente")
                                .object(
                                        CuentaProductosDto.builder()
                                                .id(cuentaProductosUpdate.getId())
                                                .producto(cuentaProductosUpdate.getProducto())
                                                .cantidad(cuentaProductosUpdate.getCantidad())
                                                .estado(cuentaProductosUpdate.getEstado())
                                                .cuenta(cuentaProductosUpdate.getCuenta())
                                                .build()
                                )
                                .build()
                        ,HttpStatus.CREATED
                );
            }
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No se ha encontrado el registro")
                            .object(null)
                            .build()
                    , HttpStatus.ALREADY_REPORTED
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
