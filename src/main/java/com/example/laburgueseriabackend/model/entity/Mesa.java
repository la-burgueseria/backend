package com.example.laburgueseriabackend.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data //anotacion de lombok crea automaticament todos los metodos basicos, setters y getters
@AllArgsConstructor // anotacion de lombok, genera un constructor con todos los argumentos de la clase
@NoArgsConstructor //anotacion de lombok, genera un constructor vacio, sin argumentos
@ToString //anotacion de lombok, genera el metodo ToString
@Builder
@Entity // anotacion que especifica que este modelo hace referencia a una entidad de la base de datos
@Table(name = "mesa") // se especifica a que tabla hace referencia
public class Mesa implements Serializable {
    @Id
    @Column(name = "id") // referencia el nombre de la columna asignada a esta variable
    @GeneratedValue(strategy = GenerationType.IDENTITY) //se especifica que el ID es autoincrement
    private Integer id;
    @Column(name = "num_mesa")
    private Integer numeroMesa;
    //relacion one to one con los códigos QR
    @OneToOne
    @JoinColumn(name = "qr_id")
    private Qr qr;
    //relacion many to one con estado mesa
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_mesa_id")
    private EstadoMesa estadoMesa;
    //relacion con cuentas
    @JsonIgnore
    @OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Cuenta> cuentas;




}
