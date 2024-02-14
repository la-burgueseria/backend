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
@CrossOrigin(origins = {"http://localhost:4200", "https://laburgueseria-ed758.web.app"})
public class AuthController {

    private final AuthImplService authService;
    private final EmpleadoImplService empleadoService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody UsuariosDto request){
        return ResponseEntity.ok(authService.login(request));
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
                        .estado(true)
                        .rol(request.getRol())
                        .build();
            }else{
                usuariosDto = UsuariosDto.builder()
                        .id(usuario.getId())
                        .nombre(request.getNombre())
                        .apellido(request.getApellido())
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .estado(true)
                        .rol(request.getRol())
                        .build();
            }
            try{
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

        usuario = authService.getUsuarioByDocumento(request.getUsername());
        empleado = empleadoService.findEmpleadoByDocumento(request.getUsername());

        if(usuario != null || empleado != null){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Este usuario ya se encuentra registrado")
                            .object(usuario)
                            .build()
                    , HttpStatus.CONFLICT
            );
        }else{
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
                    .build();
            Empleado empleadoSave = empleadoService.save(empleadoDto);

            return ResponseEntity.ok(authService.register(usuariosDto));//authService.register(request)
        }

    }
}
