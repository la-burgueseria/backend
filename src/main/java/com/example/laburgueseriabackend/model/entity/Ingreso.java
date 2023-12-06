package com.example.laburgueseriabackend.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

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
    //relacion one to many con ingresoCuenta
    @OneToMany(mappedBy = "ingreso", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    @JsonIgnore
    private List<IngresoCuenta> ingresoCuentas;
    //relacion con reporte mensual
    @OneToMany(mappedBy = "ingreso", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    @JsonIgnore
    private List<ReporteIngreso> reporteIngresos;

    @PrePersist
    protected void onCreate() {
        fecha = LocalDateTime.now();
    }

}
