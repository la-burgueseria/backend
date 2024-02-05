package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.EmpleadoDto;
import com.example.laburgueseriabackend.model.entity.Empleado;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.IEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:4200", "https://laburgueseria-ed758.web.app"})
public class EmpleadoController {
    @Autowired
    private IEmpleadoService empleadoService;

    //CREAR EMPLEADO
    // CRITERIOS:
    // no pueden haber dos numeros de
    // documento iguales
    @PostMapping("empleado")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody EmpleadoDto empleadoDto){
        Empleado empleadoSave, empleadoExists = null;

        try{
            //validar si no existe ya un empleado
            // con el mismo numero de doc
            empleadoExists = empleadoService.findEmpleadoByDocumento(empleadoDto.getDocumento());

            if(empleadoExists != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ya existe un empleado vinculado a este número de documento!")
                                .object(null)
                                .build()
                        , HttpStatus.CONFLICT
                );
            }

            //esta disponible el número de documento:
            empleadoSave = empleadoService.save(empleadoDto);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Empleado registrado exitosamente!")
                            .object(
                                    EmpleadoDto.builder()
                                            .id(empleadoSave.getId())
                                            .documento(empleadoSave.getDocumento())
                                            .nombre(empleadoSave.getNombre())
                                            .apellido(empleadoSave.getApellido())
                                            .estado(empleadoSave.getEstado())
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


    //OBTENER TODOS LOS EMPLEADOS
    @GetMapping("empleados")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){
        try{
            List<Empleado> empleados = empleadoService.listAll();

            if(empleados.isEmpty()){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("No hay registros para empleados")
                                .object(null)
                                .build()
                        , HttpStatus.NOT_FOUND
                );
            }

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("OK")
                            .object(empleados)
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

    //PAGINACION EMPLEADOS
    @GetMapping("empleados-page")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<Empleado>> empleadosPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String order,
            @RequestParam(defaultValue = "true") boolean asc
    ){
        Page<Empleado> empleados = empleadoService.empleadosPaginados(
                PageRequest.of(page, size, Sort.by(order))
        );

        if(!asc){
            empleados = empleadoService.empleadosPaginados(
                    PageRequest.of(page, size, Sort.by(order).descending())
            );
        }

        return new ResponseEntity<Page<Empleado>>(empleados, HttpStatus.OK);
    }


    //EMPLEADO POR ID
    @GetMapping("empleado/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showById(@PathVariable Integer id){
        try{
            Empleado empleado = empleadoService.findById(id);
            if(empleado != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("OK")
                                .object(
                                        EmpleadoDto.builder()
                                                .id(empleado.getId())
                                                .documento(empleado.getDocumento())
                                                .nombre(empleado.getNombre())
                                                .apellido(empleado.getApellido())
                                                .estado(empleado.getEstado())
                                                .build()
                                ).build()
                        , HttpStatus.OK
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
    //BUSCAR EMPLEADO POR DOCUMENTO
    @GetMapping("empleado/documento/{documento}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> buscarDocumento(@PathVariable Long documento){
        try{
            Empleado empleado = empleadoService.findEmpleadoByDocumento(documento);

            if(empleado != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("OK")
                                .object(
                                        EmpleadoDto.builder()
                                                .id(empleado.getId())
                                                .documento(empleado.getDocumento())
                                                .nombre(empleado.getNombre())
                                                .apellido(empleado.getApellido())
                                                .estado(empleado.getEstado())
                                                .build()
                                ).build()
                        , HttpStatus.OK
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

    //BUSCAR POR NOMBRE
    @GetMapping("empleado/nombre/{nombre}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> buscarNombre(@PathVariable String nombre){
        try{
            List<Empleado> empleados = empleadoService.findEmpleadosByNombre(nombre);
            if(!empleados.isEmpty()){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("OK")
                                .object(empleados)
                                .build()
                        , HttpStatus.OK
                );
            }
            return new ResponseEntity<>(MensajeResponse
                    .builder()
                    .mensaje("Registro no encontrado")
                    .object(empleados)
                    .build()
                    , HttpStatus.OK);
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

    //ELIMINAR EMPLEADO
    //QUEDA PENDIENTE QUE SE
    //DESHABILITE EL EMPLEADO EN VEZ DE ELIMINARSE
    // O, SI SI VA A ELIMINAR, QUE NO SE PERMITE SI
    // TIENE CUENTAS ACTIVAS
    @DeleteMapping("empleado/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        try{
            Empleado empleadoDelete = empleadoService.findById(id);
            empleadoService.delete(empleadoDelete);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("El empleado ha sido eliminado correctamente.")
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

    // ACTUALIZAR EMPLEADO
    @PutMapping("empleado/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody EmpleadoDto empleadoDto, @PathVariable Integer id){
        Empleado empleadoUpdate = null;

        try{
            if(empleadoService.existsById(id)){
                empleadoDto.setId(id);

                Empleado empleado = empleadoService.findById(id);

                //en caso de que vaya a haber un cambio de documento
                if(!empleado.getDocumento().equals(empleadoDto.getDocumento())){
                    Empleado empleadoExists = empleadoService.findEmpleadoByDocumento(empleadoDto.getDocumento());

                    if(empleadoExists == null){
                        empleadoUpdate = empleadoService.save(empleadoDto);

                        return new ResponseEntity<>(
                                MensajeResponse.builder()
                                        .mensaje("El empleado ha sido actualizado correctamente.")
                                        .object(empleadoUpdate)
                                        .build()
                                , HttpStatus.CREATED
                        );
                    }
                    return new ResponseEntity<>(MensajeResponse.builder()
                            .mensaje("Este número de documento ya esta asignado a un empleado.")
                            .object(null)
                            .build()
                            , HttpStatus.CONFLICT
                    );
                }

                empleadoUpdate = empleadoService.save(empleadoDto);

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El empleado ha sido actualizado correctamente.")
                                .object(empleadoUpdate)
                                .build()
                        , HttpStatus.CREATED
                );
            }
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Empleado no encontrado en el sistema")
                    .object(null)
                    .build()
                    , HttpStatus.NOT_FOUND
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
    @PutMapping("empleado/estado/{estado}/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateStatus(@PathVariable Boolean estado, @PathVariable Integer id){
        Empleado empleadoUpdate = null;
        EmpleadoDto empleadoDto = null;

        try{
            if(this.empleadoService.existsById(id)){
                empleadoUpdate = this.empleadoService.actualizarEstado(estado, id);

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Estado del empleado actualizado correctamente.")
                                .object(empleadoUpdate)
                                .build()
                        , HttpStatus.CREATED
                );
            }
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Empleado no encontrado en el sistema")
                    .object(null)
                    .build()
                    , HttpStatus.NOT_FOUND
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

}
