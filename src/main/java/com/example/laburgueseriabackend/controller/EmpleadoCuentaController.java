package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.EmpleadoCuentaDto;
import com.example.laburgueseriabackend.model.entity.Empleado;
import com.example.laburgueseriabackend.model.entity.EmpleadoCuenta;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.IEmpleadoCuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmpleadoCuentaController  {
    @Autowired
    private IEmpleadoCuentaService empleadoCuentaService;

    //crear empleado cuenta
    @PostMapping("empleado-cuenta")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody EmpleadoCuentaDto empleadoCuentaDto){
        EmpleadoCuenta empleadoCuentaSave = null;
        try{
            empleadoCuentaSave = empleadoCuentaService.save(empleadoCuentaDto);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Cuenta vinculada exitosamente")
                            .object(
                                    EmpleadoCuentaDto.builder()
                                            .id(empleadoCuentaSave.getId())
                                            .empleado(empleadoCuentaSave.getEmpleado())
                                            .cuenta(empleadoCuentaSave.getCuenta())
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
    //listar todas las cuentas vinculadas a  empleados
    @GetMapping("empleado-cuenta")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){
        try{
            List<EmpleadoCuenta> empleadoCuentas = empleadoCuentaService.listAll();

            if(empleadoCuentas.isEmpty()){
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
                            .object(empleadoCuentas)
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
    //BUSCAR POOR ID
    @GetMapping("empleado-cuenta/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showById(@PathVariable Integer id){
        try{
            EmpleadoCuenta empleadoCuenta = empleadoCuentaService.findById(id);
            if(empleadoCuenta != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ok, encontrado")
                                .object(
                                        EmpleadoCuentaDto.builder()
                                                .id(empleadoCuenta.getId())
                                                .cuenta(empleadoCuenta.getCuenta())
                                                .empleado(empleadoCuenta.getEmpleado())
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
    //BUSCAR EMPLEADO CUENTA POR CUENTA ID
    @GetMapping("empleado-cuenta/cuenta/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showByCuentaId(@PathVariable Integer id){
        try{
            EmpleadoCuenta empleadoCuenta = empleadoCuentaService.empleadoEnCuenta(id);

            if(empleadoCuenta != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ok, encontrado")
                                .object(
                                        EmpleadoCuentaDto.builder()
                                                .id(empleadoCuenta.getId())
                                                .cuenta(empleadoCuenta.getCuenta())
                                                .empleado(empleadoCuenta.getEmpleado())
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


    //ELIMINAR REGISTRO

    @DeleteMapping("empleado-cuenta/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        EmpleadoCuenta empleadoCuenta = null;

        try{
            empleadoCuenta = empleadoCuentaService.findById(id);
            if(empleadoCuenta != null){
                empleadoCuentaService.delete(empleadoCuenta);

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Se ha desvinculado al empleado de esta cuenta")
                                .object(empleadoCuenta)
                                .build()
                        , HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No se ha encotrado el empleado a desvincular")
                            .object(null)
                            .build()
                    , HttpStatus.NOT_FOUND
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

    //Actualizar registro
    @PutMapping("empleado-cuenta/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> update(@RequestBody EmpleadoCuentaDto empleadoCuentaDto, @PathVariable Integer id){
        EmpleadoCuenta empleadoCuentaExists, empleadoCuentaUpdate = null;

        try{
            empleadoCuentaExists = empleadoCuentaService.findById(id);

            if(empleadoCuentaExists != null){
                empleadoCuentaDto.setId(id);

                empleadoCuentaUpdate = empleadoCuentaService.save(empleadoCuentaDto);


                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Registro actualizado correctamente")
                                .object(empleadoCuentaUpdate)
                                .build()
                        , HttpStatus.CREATED
                );
            }
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No se ha encontrado el registro")
                            .object(null)
                            .build()
                    , HttpStatus.CONFLICT
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
