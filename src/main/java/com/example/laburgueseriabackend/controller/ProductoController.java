package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.CategoriaProductoDto;
import com.example.laburgueseriabackend.model.dto.InsumoDto;
import com.example.laburgueseriabackend.model.dto.ProductoDto;
import com.example.laburgueseriabackend.model.entity.Producto;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.IProductoService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductoController {
    @Autowired
    private IProductoService productoService;

    //Crear producto
    @PostMapping("producto")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody @NotNull ProductoDto productoDto){
        Producto productoSave, productoExists = null;

        try{

            productoExists = productoService.findByNombre(productoDto.getNombre());

            if(productoExists != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ya existe un producto con el mismo nombre")
                                .object(null)
                                .build()
                        , HttpStatus.OK
                );
            }

            productoSave = productoService.save(productoDto);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Producto creado exitosamente")
                            .object(
                                    Producto.builder()
                                            .id(productoSave.getId())
                                            .nombre(productoSave.getNombre())
                                            .precio(productoSave.getPrecio())
                                            .imagen(productoSave.getImagen())
                                            .descripcion(productoSave.getDescripcion())
                                            .categoriaProducto(productoSave.getCategoriaProducto())
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

    //BUSCAR PRODUCTO POR ID
    @Transactional(readOnly = true)
    @GetMapping("producto/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<?> showById(@PathVariable Integer id){
        try{
            Producto producto = productoService.findById(id);

            if(producto != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ok, encontrado")
                                .object(
                                        ProductoDto.builder()
                                                .id(producto.getId())
                                                .nombre(producto.getNombre())
                                                .precio(producto.getPrecio())
                                                .imagen(producto.getImagen())
                                                .descripcion(producto.getDescripcion())
                                                .categoriaProductoDto(
                                                        CategoriaProductoDto.builder()
                                                                .id(producto.getCategoriaProducto().getId())
                                                                .nombre(producto.getCategoriaProducto().getNombre())
                                                                .build()
                                                )
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

    //listar todos los productos
    @GetMapping("productos")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){
        List<Producto> productos = productoService.listAll();

        if(productos == null){
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
                        .object(productos)
                        .build()
                , HttpStatus.OK
        );
    }

    //Actualizar producto
    @PutMapping("producto/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody ProductoDto productoDto, @PathVariable Integer id){
        Producto productoUpdate = null;
        try{
            if(productoService.existsById(id)){
                productoDto.setId(id);
                productoUpdate = productoService.save(productoDto);

                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Actualizado correctamente")
                        .object(ProductoDto.builder()
                                .id(productoUpdate.getId())
                                .nombre(productoUpdate.getNombre())
                                .precio(productoUpdate.getPrecio())
                                .imagen(productoUpdate.getImagen())
                                .descripcion(productoUpdate.getDescripcion())
                                .categoriaProductoDto(
                                        CategoriaProductoDto.builder()
                                                .id(productoUpdate.getCategoriaProducto().getId())
                                                .nombre(productoUpdate.getCategoriaProducto().getNombre())
                                                .build()
                                )
                                .build())
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

    //ELIMINAR PRODUCTO
    @DeleteMapping("producto/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        try{
            Producto productoDelete = productoService.findById(id);
            productoService.delete(productoDelete);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Producto eliminado correctamente")
                            .object(productoDelete)
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

}