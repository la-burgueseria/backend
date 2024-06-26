package com.example.laburgueseriabackend.controller.auth;

import com.example.laburgueseriabackend.model.dao.EmpleadoDao;
import com.example.laburgueseriabackend.model.dto.EmpleadoDto;
import com.example.laburgueseriabackend.model.dto.UsuariosDto;
import com.example.laburgueseriabackend.model.entity.Empleado;
import com.example.laburgueseriabackend.model.entity.usuarios.Usuarios;
import com.example.laburgueseriabackend.model.payload.MensajeResponse;
import com.example.laburgueseriabackend.service.impl.AuthImplService;
import com.example.laburgueseriabackend.service.impl.EmpleadoImplService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthImplService authService;
    private final EmpleadoImplService empleadoService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody UsuariosDto request){
        System.out.println(request + "<=====");
        Usuarios usuario = this.authService.getUsuarioByDocumento(request.getUsername());
        if(usuario.getEstado()){
            return ResponseEntity.ok(authService.login(request));
        }else{
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Usuario inactivo")
                            .object(null)
                            .build()
                    , HttpStatus.CONFLICT
            );
        }

    }
    @GetMapping("users")
    public ResponseEntity<?> listAll(){
        List<Usuarios> usuarios = authService.listAll();
        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("Ok")
                        .object(usuarios)
                        .build()
                , HttpStatus.OK
        );
    }
    @PutMapping("users")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody UsuariosDto request){
        Usuarios usuario = null;
        Empleado empleado = null;
        usuario = authService.getUsuarioByDocumento(request.getUsername());
        empleado = empleadoService.findEmpleadoByDocumento(request.getUsername());
        EmpleadoDto empleadoDto = null;
        UsuariosDto usuariosDto = null;
        //validar que ambos existan
        if(usuario != null && empleado != null){
            empleadoDto = EmpleadoDto.builder()
                    .id(empleado.getId())
                    .nombre(request.getNombre())
                    .apellido(request.getApellido())
                    .documento(request.getUsername())
                    .estado(request.getEstado())
                    .build();
            if(request.getPassword() == null){
               usuariosDto = UsuariosDto.builder()
                        .id(usuario.getId())
                        .nombre(request.getNombre())
                        .apellido(request.getApellido())
                        .username(request.getUsername())
                        .password(usuario.getPassword())
                        .correo(request.getCorreo())
                        .estado(true)
                        .rol(request.getRol())
                        .build();
            }
            else{
                usuariosDto = UsuariosDto.builder()
                        .id(usuario.getId())
                        .nombre(request.getNombre())
                        .apellido(request.getApellido())
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .correo(request.getCorreo())
                        .estado(true)
                        .rol(request.getRol())
                        .build();
            }
            try{
                //validar si el correo electronico cambió
                 if(!usuariosDto.getCorreo().equals(usuario.getCorreo())) {
                     //si el correo es distinto, validar si ya existe un usuario con ese correo vinculado
                     Usuarios correoExists = null;
                     correoExists = authService.findUsuariosByCorreo(usuariosDto.getCorreo());
                     //en caso de que si exista un usuario con el correo vinculado
                     //retorna mensaje de error
                     if (correoExists != null) {
                         return new ResponseEntity<>(
                                 MensajeResponse.builder()
                                         .mensaje("Este correo electronico ya está en uso.")
                                         .object(null)
                                         .build()
                                 , HttpStatus.NOT_ACCEPTABLE
                         );
                     }
                     //si el correo no está vinculado a ningun usuario lo actualiza sin problema
                     else {
                         Empleado empleadoSave = empleadoService.save(empleadoDto);
                         authService.updateUsuario(usuariosDto);

                         return new ResponseEntity<>(
                                 MensajeResponse.builder()
                                         .mensaje("Usuario actualizado exitosamente")
                                         .object(null)
                                         .build()
                                 , HttpStatus.OK
                         );
                     }
                 }
                //en caso de que el correo no vaya a recibir modificaciones, se procede sin problema
                Empleado empleadoSave = empleadoService.save(empleadoDto);
                authService.updateUsuario(usuariosDto);

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Usuario actualizado exitosamente")
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
        }else{
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No se encontraron los usuarios")
                            .object(null)
                            .build()
                    , HttpStatus.CONFLICT
            );
        }
    }
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody UsuariosDto request){
        Usuarios usuario = null;
        Empleado empleado = null;
        Usuarios correoExists = null;

        usuario = authService.getUsuarioByDocumento(request.getUsername());
        empleado = empleadoService.findEmpleadoByDocumento(request.getUsername());
        correoExists = authService.findUsuariosByCorreo(request.getCorreo());
        if(usuario != null || empleado != null){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Este usuario ya se encuentra registrado")
                            .object(usuario)
                            .build()
                    , HttpStatus.CONFLICT
            );
        }
        else if(correoExists != null){ //validar que el correo no este en uso
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Este correo electronico ya se encuentra en uso")
                            .object(usuario)
                            .build()
                    , HttpStatus.NOT_ACCEPTABLE
            );
        }
        else{
            EmpleadoDto empleadoDto = EmpleadoDto.builder()
                    .id(0)
                    .nombre(request.getNombre())
                    .apellido(request.getApellido())
                    .documento(request.getUsername())
                    .estado(true)
                    .build();
            UsuariosDto usuariosDto = UsuariosDto.builder()
                    .id(0)
                    .nombre(request.getNombre())
                    .apellido(request.getApellido())
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .estado(true)
                    .rol(request.getRol())
                    .correo(request.getCorreo())
                    .build();
            Empleado empleadoSave = empleadoService.save(empleadoDto);

            return ResponseEntity.ok(authService.register(usuariosDto));//authService.register(request)
        }

    }
    //controller para asignar token
    @PutMapping("users/token/{correo}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> setToken(@PathVariable String correo){
        //validar que el correo exista
        try{
            Usuarios correoExists = null;
            correoExists = this.authService.findUsuariosByCorreo(correo);
            if(correoExists == null){
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("")
                                .object(null)
                                .build()
                        , HttpStatus.NOT_FOUND
                );
            }
            //generar el token
            TokenGenerator tokenGenerator = new TokenGenerator();
            String token = tokenGenerator.generateToken();
            //asignar el token al usuario
            this.authService.updateToken(token, correoExists.getId());//guardarlo en la bd
            //retornar el token
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(token)
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
    //obtener usuario con el correo
    @GetMapping("users/correo/{correo}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getUsuarioByCorreo(@PathVariable String correo){
        try{
            UsuariosDto usuariosDto = null;
            Usuarios usuario = null;

            usuario = this.authService.findUsuariosByCorreo(correo);

            if(usuario != null){

                //generar el token
                TokenGenerator tokenGenerator = new TokenGenerator();
                String token = tokenGenerator.generateToken();
                //asignar el token al usuario
                this.authService.updateToken(token, usuario.getId());//guardarlo en la bd

                usuariosDto = UsuariosDto.builder()
                        .id(usuario.getId())
                        .nombre(usuario.getNombre())
                        .apellido(usuario.getApellido())
                        .correo(usuario.getCorreo())
                        .username(usuario.getUsername())
                        .password("null")
                        .rol(usuario.getRol())
                        .estado(usuario.getEstado())
                        .token(token)
                        .build();
            }

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("OK")
                            .object(usuariosDto)
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

    //recibir token y correo electronico
    @GetMapping("users/correo/{correo}/{token}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> validateTokenByMail(@PathVariable String correo, @PathVariable String token){
        try{
            UsuariosDto usuariosDto = null;
            Usuarios usuario = null;
            System.out.println(token);
            usuario = this.authService.findUsuariosByCorreo(correo);

            if(usuario != null){
                System.out.println(usuario.getToken());
                if(usuario.getToken().equals(token)){
                    return new ResponseEntity<>(
                            MensajeResponse.builder()
                                    .mensaje("OK")
                                    .object(null)
                                    .build()
                            , HttpStatus.OK
                    );
                }
            }
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Conflicto")
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

    //activar / desactivar usuario
    @PatchMapping("users/status")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateUserStatus(@RequestBody UsuariosDto usuariosDto){
        Usuarios usuario = null;
        try{
            usuario = this.authService.findUsuariosByCorreo(usuariosDto.getCorreo());
            if(usuario != null){
                usuario.setEstado(usuariosDto.getEstado());
                this.authService.updateEstado(usuario.getId(), usuario.getEstado());

                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Estado cambiado exitosamente")
                                .object(null)
                                .build()
                        , HttpStatus.OK
                );
            }

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No se ha encontrado el usuario con este correo electronico")
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
}

