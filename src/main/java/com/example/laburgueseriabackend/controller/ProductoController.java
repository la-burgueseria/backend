package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.CategoriaProductoDto;
import com.example.laburgueseriabackend.model.dto.ProductoDto;
import com.example.laburgueseriabackend.model.entity.CategoriaProducto;
import com.example.laburgueseriabackend.model.entity.Producto;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.IProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:4200", "https://laburgueseria-ed758.web.app"})
public class ProductoController {
    @Autowired
    private IProductoService productoService;

    //Crear producto
    @PostMapping("producto")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(
            @RequestBody byte[] imagen,
                                    @RequestParam("nombre") String nombre,
                                    @RequestParam("precio") Double precio,
                                    @RequestParam("descripcion") String descripcion,
                                    @RequestParam("categoria") Integer categoria)
                                    {


        Producto productoSave, productoExists = null;
        ProductoDto productoDto = null;

        try{
           ProductoDto producto = ProductoDto.builder()
                   .id(0)
                   .nombre(nombre)
                   .precio(precio)
                   .descripcion(descripcion)
                   .imagen(null)
                   .categoriaProductoDto(
                           CategoriaProductoDto.builder()
                                   .id(categoria)
                                   .build()
                   ).build();


            if(imagen == null){
                productoDto = ProductoDto.builder()
                        .id(0)
                        .nombre(producto.getNombre())
                        .precio(producto.getPrecio())
                        .descripcion(producto.getDescripcion())
                        .imagen(null)
                        .categoriaProductoDto(
                                CategoriaProductoDto.builder()
                                        .id(producto.getCategoriaProductoDto().getId())
                                        .nombre("")
                                        .build()
                        )
                        .build();
            }else {
                productoDto = ProductoDto.builder()
                        .id(0)
                        .nombre(producto.getNombre())
                        .precio(producto.getPrecio())
                        .descripcion(producto.getDescripcion())
                        .imagen(imagen)
                        .categoriaProductoDto(
                                CategoriaProductoDto.builder()
                                        .id(producto.getCategoriaProductoDto().getId())
                                        .nombre("")
                                        .build()
                        )
                        .build();
            }
            productoExists = productoService.findByNombre(productoDto.getNombre());

            if(productoExists != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ya existe un producto con el mismo nombre")
                                .object(null)
                                .build()
                        , HttpStatus.CONFLICT
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

        if(productos.isEmpty()){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No hay registros en el sistema")
                            .object(productos)
                            .build()
                    , HttpStatus.OK
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
    //Cambiar estado isPublicado
    @PutMapping("producto/{id}/{estado}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> changeIsPublicado(@PathVariable Integer id, @PathVariable Boolean estado){
        Producto productoUpdate = null;

        try{
            productoUpdate =  this.productoService.changeIsPublicado(id, estado);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Actualizado con exito")
                            .object(productoUpdate)
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
    //Actualizar producto
    @PutMapping("producto")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(
            @RequestBody byte[] imagen,
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") Double precio,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("categoria") Integer categoria,
            @RequestParam("id") Integer idProducto
    )
{

        ProductoDto productoDto = null;



        productoDto = ProductoDto.builder()
                .id(idProducto)
                .nombre(nombre)
                .precio(precio)
                .descripcion(descripcion)
                .imagen(imagen)
                .categoriaProductoDto(
                        CategoriaProductoDto.builder()
                                .id(categoria)
                                .nombre("")
                                .build()
                )
                .build();

        Producto productoUpdate = null;
        try{
            if(productoService.existsById(idProducto)){

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
            }else{
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Registro no encontrado en la base de datos")
                        .object(null)
                        .build()
                        , HttpStatus.NOT_FOUND
                );
            }

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

    //paginacion
    @GetMapping("productos-page")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<Producto>> productosPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "nombre") String order,
            @RequestParam(defaultValue = "true") boolean asc
    ){
        Page<Producto> productos = productoService.productosPaginados(
                PageRequest.of(page, size, Sort.by(order))
        );
        if(!asc){
            productos = productoService.productosPaginados(
                    PageRequest.of(page, size, Sort.by(order).descending())
            );
        }
        return new ResponseEntity<Page<Producto>>(productos, HttpStatus.OK);
    }

    //buscar producto por nombre
    @GetMapping("producto/buscar/{nombre}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> buscarPorNombre(@PathVariable String nombre){
        List<Producto> productos;

        try{
            productos = productoService.findProductoByNombre(nombre.toUpperCase());
            if(productos.isEmpty()){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("OK")
                                .object(productos)
                                .build()
                        , HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("ok")
                            .object(productos)
                            .build()
                    , HttpStatus.OK
            );
        }catch(DataAccessException exDt){

            return new ResponseEntity<>(MensajeResponse
                    .builder()
                    .mensaje(exDt.getMessage())
                    .object(null)
                    .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
