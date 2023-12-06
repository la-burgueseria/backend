package com.example.laburgueseriabackend.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "cuenta")
public class Cuenta implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    // ORGANIZAR RELACION ENTRE CUENTA Y PRODUCTOS CON LA ENTIDAD INTERMEDIA
    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<CuentaProductos> cuentaProductos;

    //relacion many to one con estado_cuenta
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_cuenta_id")
    private EstadoCuenta estadoCuenta;
    //relacion  one to manycon ingreso_cuenta
    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    @JsonIgnore
    private List<IngresoCuenta> ingresoCuentas;
    //relacion con mesas
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;


    //relacion con empleado_cuenta
    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    @JsonIgnore
    private List<EmpleadoCuenta> empleadoCuentas;


    @PrePersist
    protected void onCreate() {
        fecha = LocalDateTime.now();
    }
}
