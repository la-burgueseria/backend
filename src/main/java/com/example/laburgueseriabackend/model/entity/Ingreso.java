package com.example.laburgueseriabackend.model.entity;


import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Data //anotacion de lombok crea automaticament todos los metodos basicos, setters y getters
@AllArgsConstructor // anotacion de lombok, genera un constructor con todos los argumentos de la clase
@NoArgsConstructor //anotacion de lombok, genera un constructor vacio, sin argumentos
@ToString //anotacion de lombok, genera el metodo ToString
@Builder
@Entity
@Table(name = "ingreso")
public class Ingreso implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY) //se especifica que el ID es autoincrement
    private Integer id;
    @Column(name = "fecha")
    private LocalDateTime fecha;
    @Column(name = "metodo_pago")
    private String metodoPago;
    @Column(name = "total")
    private Double total;
    //relacion con cuenta
    @OneToOne
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;
    @PrePersist
    protected void onCreate() {
        fecha = LocalDateTime.now(ZoneOffset.UTC);
    }

}
