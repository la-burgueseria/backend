package com.example.laburgueseriabackend.controller;

import com.example.laburgueseriabackend.model.dto.InsumoDto;
import com.example.laburgueseriabackend.model.dto.InsumosPorProductoDto;
import com.example.laburgueseriabackend.model.dto.ProductoDto;
import com.example.laburgueseriabackend.model.entity.Insumo;
import com.example.laburgueseriabackend.model.entity.InsumosPorProducto;
import com.example.laburgueseriabackend.model.entity.Producto;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.IInsumoService;
import com.example.laburgueseriabackend.service.IInsumosPorProductoService;
import com.example.laburgueseriabackend.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:4200", "https://laburgueseria-ed758.web.app"})
public class InsumosPorProductoController {
    @Autowired
    private IInsumosPorProductoService insumosPorProductoService;
    @Autowired
    private IInsumoService insumoService;
    @Autowired
    private IProductoService productoService;
    //crear insumo por producto
    @PostMapping("insumo-por-producto")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> save(@RequestBody InsumosPorProductoDto insumosPorProductoDto
    , @RequestParam("idInsumo") Integer insumoId
    , @RequestParam("idProducto") Integer productoId){
        InsumosPorProducto insumosPorProductoSave = null;
        List<InsumosPorProducto> insumosPorProductoExists = null;
        Integer idInsumo = insumoId;
        Integer idProducto = productoId;

        //construir nuevamente el ippDto (no llegan los objetos insumos ni producto dentro de el)
        Insumo insumo = this.insumoService.findById(idInsumo);
        Producto producto = this.productoService.findById(idProducto);

        insumosPorProductoDto = InsumosPorProductoDto.builder()
                .id(insumosPorProductoDto.getId())
                .cantidad(insumosPorProductoDto.getCantidad())
                .insumoDto(insumo)
                .productoDto(producto)
                .build();


        try{
            //validar si el insumo que se desea vincular al producto no está ya vinculado con anterioridad
            insumosPorProductoExists = insumosPorProductoService.insumoPorProductoExists(idInsumo, idProducto);

            //si no esta vinculado
            if(insumosPorProductoExists.isEmpty()){
                insumosPorProductoSave = insumosPorProductoService.save(insumosPorProductoDto);

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Creado exitosamente")
                                .object(
                                        InsumosPorProducto.builder()
                                                .id(insumosPorProductoSave.getId())
                                                .insumo(insumosPorProductoSave.getInsumo())
                                                .producto(insumosPorProductoSave.getProducto())
                                                .cantidad(insumosPorProductoSave.getCantidad())
                                                .build()
                                ).build()
                        , HttpStatus.CREATED
                );
            }
            //si ya esta vinculado
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("El insumo que se desea vincular ya está asignado a este producto")
                            .object(null)
                            .build()
                    , HttpStatus.ALREADY_REPORTED
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

    // get by id
    @GetMapping("insumo-por-producto/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showById(@PathVariable Integer id){
        try{
            InsumosPorProducto insumosPorProducto = insumosPorProductoService.findById(id);

            if(insumosPorProducto != null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Ok, encontrado")
                                .object(
                                        InsumosPorProductoDto.builder()
                                                .id(insumosPorProducto.getId())
                                                .cantidad(insumosPorProducto.getCantidad())
                                                .insumoDto(insumosPorProducto.getInsumo())
                                                .productoDto(
                                                        Producto.builder()
                                                                .id(insumosPorProducto.getProducto().getId())
                                                                .nombre(insumosPorProducto.getProducto().getNombre())
                                                                .precio(insumosPorProducto.getProducto().getPrecio())
                                                                .imagen(insumosPorProducto.getProducto().getImagen())
                                                                .descripcion(insumosPorProducto.getProducto().getDescripcion())
                                                                .build()
                                                )
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

    //LISTAR TODOS LOS INSUMOS POR PRODUCTOS
    @GetMapping("insumos-por-productos")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){

        try{
            List<InsumosPorProducto> insumosPorProductos = insumosPorProductoService.listAll();

            if(insumosPorProductos.isEmpty()){
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
                            .object(insumosPorProductos)
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

    //obtener los insumos vinculados a un producto dado
    @GetMapping("insumo-por-producto/producto/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> insumosProducto(@PathVariable Integer id){
        try{
            List<InsumosPorProducto> insumosProductos = this.insumosPorProductoService.selecionarInsumosDelProducto(id);
            if(insumosProductos.isEmpty()){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("No hay registros en el sistema")
                                .object(null)
                                .build()
                        , HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Ok")
                            .object(insumosProductos)
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

    //eliminar insumo
    @DeleteMapping("insumo-por-producto/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        try{
            InsumosPorProducto insumosPorProducto = insumosPorProductoService.findById(id);
            insumosPorProductoService.delete(insumosPorProducto);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Producto eliminado correctamente")
                            .object(insumosPorProducto)
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

    //  ACTUALIZAR REGISTRO
    @PutMapping("insumo-por-producto/{id}/{insumoId}/{productoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody InsumosPorProductoDto insumosPorProductoDto,
                                    @PathVariable Integer id,
                                    @PathVariable Integer insumoId,
                                    @PathVariable Integer productoId){
        System.out.println(insumosPorProductoDto);
        InsumosPorProducto insumosPorProductoUpdate = null;
        List<InsumosPorProducto> insumosPorProductoExists = null;
        Integer idInsumo = insumoId;
        Integer idProducto = productoId;
        Insumo insumo = null;
        Producto producto = null;
        try{
            insumo = this.insumoService.findById(idInsumo);
            producto = this.productoService.findById(idProducto);
            insumosPorProductoExists = insumosPorProductoService.insumoPorProductoExists(idInsumo, idProducto);
            if(insumosPorProductoExists.isEmpty()){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("No hay ningún insumo asignado a este producto con esta referencia: "+ id)
                                .object(null)
                                .build()
                        , HttpStatus.NOT_FOUND
                );
            }

            insumosPorProductoDto.setId(id);

            insumosPorProductoDto.setInsumoDto(insumo);
            insumosPorProductoDto.setProductoDto(producto);
            insumosPorProductoUpdate = insumosPorProductoService.save(insumosPorProductoDto);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Actualizado exitosamente")
                            .object(
                                    InsumosPorProducto.builder()
                                            .id(insumosPorProductoUpdate.getId())
                                            .insumo(insumosPorProductoUpdate.getInsumo())
                                            .producto(insumosPorProductoUpdate.getProducto())
                                            .cantidad(insumosPorProductoUpdate.getCantidad())
                                            .build()
                            ).build()
                    , HttpStatus.CREATED);


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
