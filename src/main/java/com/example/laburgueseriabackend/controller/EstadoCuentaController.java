package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.EstadoCuentaDto;
import com.example.laburgueseriabackend.model.entity.EstadoCuenta;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.IEstadoCuentaService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EstadoCuentaController {
    @Autowired
    private IEstadoCuentaService estadoCuentaService;

    //LISTAR TODOS LOS ESTADOS
    @GetMapping("estado-cuentas")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){
        try{
            List<EstadoCuenta> estadosCuentas = estadoCuentaService.listAll();
            if(estadosCuentas.isEmpty()){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("No existen registros actualmente")
                                .object(null)
                                .build()
                        , HttpStatus.OK
                );
            }
            //En caso de haber registros retorna la lista
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ok")
                            .object(estadosCuentas)
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
    //CREAR NUEVO ESTADO
    @PostMapping("estado-cuenta")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody EstadoCuentaDto estadoCuentaDto){
        EstadoCuenta estadoCuentaSave = null;
        EstadoCuenta estadoCuentaExists = null;

        try{
            estadoCuentaExists = estadoCuentaService.findByNombre(estadoCuentaDto.getNombre());

            if(estadoCuentaExists != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ya existe una categoría con el mismo nombre")
                                .object(null)
                                .build()
                        , HttpStatus.CONFLICT
                );
            }

            estadoCuentaSave = estadoCuentaService.save(estadoCuentaDto);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Estado creado correctamente")
                            .object(
                                    EstadoCuentaDto.builder()
                                            .id(estadoCuentaSave.getId())
                                            .nombre(estadoCuentaSave.getNombre())
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
    //ACTUALIZAR ESTADO
    @PutMapping("estado-cuenta/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody EstadoCuentaDto estadoCuentaDto,@PathVariable Integer id){
        EstadoCuenta estadoCuentaUpdate = null;

        try{
            if(estadoCuentaService.existsById(id)){
                estadoCuentaDto.setId(id);

                estadoCuentaUpdate = estadoCuentaService.save(estadoCuentaDto);

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Estado creado correctamente")
                                .object(
                                        EstadoCuentaDto.builder()
                                                .id(estadoCuentaUpdate.getId())
                                                .nombre(estadoCuentaUpdate.getNombre())
                                                .build()
                                )
                                .build()
                        , HttpStatus.CREATED
                );
            }
            //Si no existe:
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No se ha encontrado el registro")
                            .object(null)
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

    //ELIMINAR ESTADO
    @DeleteMapping("estado-cuenta/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        try{
            if(estadoCuentaService.existsById(id)){
                EstadoCuenta estadoCuentaDelete = estadoCuentaService.findById(id);

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El registro ha sido eliminado exitosamente")
                                .object(null)
                                .build()
                        , HttpStatus.NO_CONTENT
                );
            }
            //en caso de NO existir
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("El registro que se intenta eliminar no existe.")
                            .object(null)
                            .build()
                    , HttpStatus.NOT_FOUND
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

    // BUSCAR ESTADO POR ID
    @GetMapping("estado-cuenta/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<?> showById(@PathVariable Integer id){
        try{
            EstadoCuenta estadoCuenta = estadoCuentaService.findById(id);

            if(estadoCuenta != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ok, encontrado")
                                .object(
                                        EstadoCuentaDto.builder()
                                                .id(estadoCuenta.getId())
                                                .nombre(estadoCuenta.getNombre())
                                                .cuentas(estadoCuenta.getCuentas())
                                                .build()
                                )
                                .build()
                        , HttpStatus.OK
                );
            }
            return new ResponseEntity<>(MensajeResponse
                    .builder()
                    .mensaje("Registro no encontrado")
                    .object(null)
                    .build()
                    , HttpStatus.NOT_FOUND);
        }catch (DataAccessException exdt){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exdt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
