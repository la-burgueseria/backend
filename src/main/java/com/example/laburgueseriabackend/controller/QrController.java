package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.QrDto;
import com.example.laburgueseriabackend.model.entity.Qr;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.IQrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class QrController {
    @Autowired
    private IQrService qrService;

    //LISTAR TODOS LOS QR
    @GetMapping("qrs")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){
        try{
            List<Qr> qrs = qrService.listAll();
            if(qrs.isEmpty()){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Aún no existe ningún QR vinculado a una mesa")
                                .object(null)
                                .build()
                        , HttpStatus.OK
                );
            }

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ok")
                            .object(qrs)
                            .build()
                    , HttpStatus.OK
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

    //CREAR QR
    @PostMapping("qr")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody QrDto qrDto){
        Qr qrSave, qrExists = null;

        try{
            qrExists = qrService.findByRuta(qrDto.getUrl());

            if(qrExists == null){
                qrSave = qrService.save(qrDto);

                qrDto = QrDto.builder()
                        .id(qrSave.getId())
                        .ruta(qrSave.getRuta())
                        .url(qrSave.getUrl())
                        .build();

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Qr creado exitosamente")
                                .object(qrDto)
                                .build()
                        , HttpStatus.CREATED
                );
            }

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ya existe un qr vinculado a esta url")
                            .object(null)
                            .build()
                    , HttpStatus.CONFLICT
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

    //ACTUALIZAR QR
    @PutMapping("qr/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody QrDto qrDto, @PathVariable Integer id){
        Qr qrUpdate = null;

        try{
            if(qrService.existsById(id)){
                qrDto.setId(id);
                qrUpdate = qrService.save(qrDto);

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Qr actualizaco correctamente")
                                .object(
                                        QrDto.builder()
                                                .id(qrUpdate.getId())
                                                .ruta(qrUpdate.getRuta())
                                                .url(qrUpdate.getUrl())
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
        }catch (DataAccessException exDt){
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje(exDt.getMessage())
                    .object(null)
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    //ELIMINAR QR
    @DeleteMapping("qr/({id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        try{
            Qr qrDelete = qrService.findById(id);
            qrService.delete(qrDelete);
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Qr eliminado correctamente")
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

    //BURCAR QR POR ID
    @GetMapping("qr/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showById(@PathVariable Integer id){
        Qr qr = null;
        QrDto qrDto = null;
        try{
            qr = qrService.findById(id);

            if(qr == null){
                return new ResponseEntity<>(MensajeResponse
                        .builder()
                        .mensaje("Registro no encontrado")
                        .object(null)
                        .build()
                        , HttpStatus.INTERNAL_SERVER_ERROR);
            }

            qrDto = QrDto.builder()
                    .id(qr.getId())
                    .ruta(qr.getRuta())
                    .url(qr.getUrl())
                    .build();

            return new ResponseEntity<>(MensajeResponse
                    .builder()
                    .mensaje("OK")
                    .object(qrDto)
                    .build()
                    , HttpStatus.OK
            );
        }catch (DataAccessException exDt){
            return new ResponseEntity<>(MensajeResponse
                    .builder()
                    .mensaje("Registro no encontrado")
                    .object(null)
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
