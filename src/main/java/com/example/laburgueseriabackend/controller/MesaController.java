package com.example.laburgueseriabackend.controller;


import com.example.laburgueseriabackend.model.dto.EstadoMesaDto;
import com.example.laburgueseriabackend.model.dto.MesaDto;
import com.example.laburgueseriabackend.model.dto.QrDto;
import com.example.laburgueseriabackend.model.entity.Mesa;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.IMesaService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class MesaController {
    @Autowired
    private IMesaService mesaService;

    //crear mesa
    @PostMapping("mesa")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody @NotNull MesaDto mesaDto){
        Mesa mesaSave, mesaExists = null;

        try{
            mesaExists = mesaService.findByNumMesa(mesaDto.getNumeroMesa());

            if(mesaExists != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ya existe una mesa con el mismo número asignado")
                                .object(null)
                                .build()
                        , HttpStatus.CONFLICT
                );
            }

            mesaSave = mesaService.save(mesaDto);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Mesa creada correctamente")
                            .object(
                                    MesaDto.builder()
                                            .id(mesaSave.getId())
                                            .numeroMesa(mesaSave.getNumeroMesa())
                                            .qr(QrDto.builder()
                                                    .id(mesaSave.getQr().getId())
                                                    .ruta(mesaSave.getQr().getRuta())
                                                    .url(mesaSave.getQr().getUrl())
                                                    .build())
                                            .estadoMesa(EstadoMesaDto.builder()
                                                    .id(mesaSave.getEstadoMesa().getId())
                                                    .nombre(mesaSave.getEstadoMesa().getNombre())
                                                    .build())
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

    //LISTAR TODAS LAS MESAS
    @GetMapping("mesas")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){
        try{
            List<Mesa> mesas = mesaService.listAll();

            if(mesas.isEmpty()){
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
                            .object(mesas)
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
    //MESA POR ID
    @GetMapping("mesa/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<?> showById(@PathVariable Integer id){
        try{
            Mesa mesa = mesaService.findById(id);
            if(mesa != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ok")
                                .object(
                                        MesaDto.builder()
                                                .id(mesa.getId())
                                                .numeroMesa(mesa.getNumeroMesa())
                                                .qr(QrDto.builder()
                                                        .id(mesa.getQr().getId())
                                                        .ruta(mesa.getQr().getRuta())
                                                        .url(mesa.getQr().getUrl())
                                                        .build())
                                                .estadoMesa(EstadoMesaDto.builder()
                                                        .id(mesa.getEstadoMesa().getId())
                                                        .nombre(mesa.getEstadoMesa().getNombre())
                                                        .build())
                                                .build()
                                )
                                .build()
                        , HttpStatus.CREATED
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
    //ELIMINAR MESA
    @DeleteMapping("mesa/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        try{
            Mesa mesaDelete = mesaService.findById(id);
            mesaService.delete(mesaDelete);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Esta mesa ha sido eliminada correctamente")
                            .object(mesaDelete)
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

    //ACTUALIZAR MESA
    //Falta:
    //* Validar que si se cambia el número de mesa no coincida
    //  con alguno registrado en la bse de datos (que no esta el del objeto en cuestion)
    //* Que no se pueda vincular con un id de qr que ya este vinculado a otra mesa
    @PutMapping("mesa/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody MesaDto mesaDto, @PathVariable Integer id){
        Mesa mesaUpdate = null;
        try{
            if(mesaService.existsById(id)){
                mesaDto.setId(id);
                //en caso de cambiar el numero de mesa
                //validar que el número de mesa a actualizar no este en una mesa ya existente
                Mesa mesa = mesaService.findById(id);
                //en caso de que los numeros de mesa no coincidan
                if(mesa.getNumeroMesa() != mesaDto.getNumeroMesa()){
                    Mesa mesaExists = mesaService.findByNumMesa(mesaDto.getNumeroMesa());

                    //en caso de no haber ninguna mesa con el numero nuevo
                    if(mesaExists == null){
                        //validar en casi de que se vaya a cambiar el número de qr
                        //en caso de ser distintos
                        if(mesa.getQr().getId() != mesaDto.getQr().getId()){
                            Mesa mesaQrId = mesaService.findByQrId(mesaDto.getQr().getId());
                            //si el nuevo número de qr esta libre
                            if(mesaQrId == null){
                                mesaUpdate = mesaService.save(mesaDto);

                                return new ResponseEntity<>(
                                        MensajeResponse.builder()
                                                .mensaje("Ok")
                                                .object(
                                                        MesaDto.builder()
                                                                .id(mesaUpdate.getId())
                                                                .numeroMesa(mesaUpdate.getNumeroMesa())
                                                                .qr(QrDto.builder()
                                                                        .id(mesaUpdate.getQr().getId())
                                                                        .ruta(mesaUpdate.getQr().getRuta())
                                                                        .url(mesaUpdate.getQr().getUrl())
                                                                        .build())
                                                                .estadoMesa(EstadoMesaDto.builder()
                                                                        .id(mesaUpdate.getEstadoMesa().getId())
                                                                        .nombre(mesaUpdate.getEstadoMesa().getNombre())
                                                                        .build())
                                                                .build()
                                                )
                                                .build()
                                        , HttpStatus.CREATED
                                );
                            }
                            //si el número de qr ya está en uso
                            return new ResponseEntity<>(MensajeResponse.builder()
                                    .mensaje("Ya existe una mesa con el mismo QR asginado")
                                    .object(null)
                                    .build()
                                    , HttpStatus.CONFLICT
                            );
                        }
                        //si no se hara cambio de numero de qr
                        else if(mesa.getQr().getId() == mesaDto.getQr().getId()){
                            mesaUpdate = mesaService.save(mesaDto);

                            return new ResponseEntity<>(
                                    MensajeResponse.builder()
                                            .mensaje("Ok")
                                            .object(
                                                    MesaDto.builder()
                                                            .id(mesaUpdate.getId())
                                                            .numeroMesa(mesaUpdate.getNumeroMesa())
                                                            .qr(QrDto.builder()
                                                                    .id(mesaUpdate.getQr().getId())
                                                                    .ruta(mesaUpdate.getQr().getRuta())
                                                                    .url(mesaUpdate.getQr().getUrl())
                                                                    .build())
                                                            .estadoMesa(EstadoMesaDto.builder()
                                                                    .id(mesaUpdate.getEstadoMesa().getId())
                                                                    .nombre(mesaUpdate.getEstadoMesa().getNombre())
                                                                    .build())
                                                            .build()
                                            )
                                            .build()
                                    , HttpStatus.CREATED
                            );
                        }


                    }
                    else{
                        return new ResponseEntity<>(MensajeResponse.builder()
                                .mensaje("Ya existe una mesa con el mismo número asignado")
                                .object(null)
                                .build()
                                , HttpStatus.CONFLICT
                        );
                    }
                }
                //en caso de que los numeros de mesa si sean los mismos
                if (mesa.getNumeroMesa() == mesaDto.getNumeroMesa()) {

                    if(mesa.getQr().getId() != mesaDto.getQr().getId()) {
                        Mesa mesaQrId = mesaService.findByQrId(mesaDto.getQr().getId());
                        if(mesaQrId == null){
                            mesaUpdate = mesaService.save(mesaDto);

                            return new ResponseEntity<>(
                                    MensajeResponse.builder()
                                            .mensaje("Ok")
                                            .object(
                                                    MesaDto.builder()
                                                            .id(mesaUpdate.getId())
                                                            .numeroMesa(mesaUpdate.getNumeroMesa())
                                                            .qr(QrDto.builder()
                                                                    .id(mesaUpdate.getQr().getId())
                                                                    .ruta(mesaUpdate.getQr().getRuta())
                                                                    .url(mesaUpdate.getQr().getUrl())
                                                                    .build())
                                                            .estadoMesa(EstadoMesaDto.builder()
                                                                    .id(mesaUpdate.getEstadoMesa().getId())
                                                                    .nombre(mesaUpdate.getEstadoMesa().getNombre())
                                                                    .build())
                                                            .build()
                                            )
                                            .build()
                                    , HttpStatus.CREATED
                            );
                        }
                        return new ResponseEntity<>(MensajeResponse.builder()
                                .mensaje("Ya existe una mesa con el mismo QR asginado")
                                .object(null)
                                .build()
                                , HttpStatus.CONFLICT
                        );
                    }
                    else if(mesa.getQr().getId() == mesaDto.getQr().getId()){
                        mesaUpdate = mesaService.save(mesaDto);

                        return new ResponseEntity<>(
                                MensajeResponse.builder()
                                        .mensaje("Ok")
                                        .object(
                                                MesaDto.builder()
                                                        .id(mesaUpdate.getId())
                                                        .numeroMesa(mesaUpdate.getNumeroMesa())
                                                        .qr(QrDto.builder()
                                                                .id(mesaUpdate.getQr().getId())
                                                                .ruta(mesaUpdate.getQr().getRuta())
                                                                .url(mesaUpdate.getQr().getUrl())
                                                                .build())
                                                        .estadoMesa(EstadoMesaDto.builder()
                                                                .id(mesaUpdate.getEstadoMesa().getId())
                                                                .nombre(mesaUpdate.getEstadoMesa().getNombre())
                                                                .build())
                                                        .build()
                                        )
                                        .build()
                                , HttpStatus.CREATED
                        );
                    }

                }


            }
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Registro no encontrado en la base de datos")
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
